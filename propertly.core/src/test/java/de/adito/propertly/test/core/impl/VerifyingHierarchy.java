package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.Set;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 20:07
 */
public class VerifyingHierarchy<T extends IPropertyPitProvider> extends Hierarchy<T>
{

  public VerifyingHierarchy(String pName, T pPPP)
  {
    super(pHierarchy -> {
      return new _VerifyingNode(pHierarchy, null, PropertyDescription.create(
          IPropertyPitProvider.class, pPPP.getClass(), pName));
    }, pPPP);
  }


  /**
   * Node-Impl
   */
  private static class _VerifyingNode extends Node
  {
    _VerifyingNode(@Nonnull Hierarchy pHierarchy, @Nullable AbstractNode pParent, @Nonnull IPropertyDescription pPropertyDescription)
    {
      super(pHierarchy, pParent, pPropertyDescription);
    }

    @Override
    public Object setValue(Object pValue, @Nonnull Set<Object> pAttributes)
    {
      if (pValue instanceof Number)
      {
        IntVerifier verifier = getProperty().getDescription().getAnnotation(IntVerifier.class);
        if (verifier != null)
        {
          Number value = (Number) pValue;
          if (value.doubleValue() < verifier.minValue() || value.doubleValue() > verifier.maxValue())
            throw new IllegalArgumentException(value.toString() + " for " + getProperty());
        }
      }
      return super.setValue(pValue, pAttributes);
    }

    @Override
    public boolean canRead()
    {
      AccessModifier mod = getProperty().getDescription().getAnnotation(AccessModifier.class);
      return mod == null || mod.canRead();
    }

    @Override
    public boolean canWrite()
    {
      AccessModifier mod = getProperty().getDescription().getAnnotation(AccessModifier.class);
      return mod == null || mod.canWrite();
    }

    @Override
    protected INode createChild(IPropertyDescription pPropertyDescription)
    {
      ensureValid();
      return new _VerifyingNode(getHierarchy(), this, PropertyDescription.create(pPropertyDescription));
    }
  }

}
