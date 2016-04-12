package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.*;
import java.util.Set;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 20:07
 */
public class VerifyingHierarchy<T extends IPropertyPitProvider> extends DelegatingHierarchy<T>
{

  public VerifyingHierarchy(Hierarchy<T> pSourceHierarchy)
  {
    super(pSourceHierarchy, (pHierarchy, pSourceNode) -> new _VerifyingNode(pHierarchy, null, pSourceNode));
  }


  /**
   * Node-Impl
   */
  private static class _VerifyingNode extends DelegatingNode
  {
    _VerifyingNode(@Nonnull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent, @Nonnull INode pDelegate)
    {
      super(pHierarchy, pParent, pDelegate);
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
    protected DelegatingNode createChild(INode pDelegate)
    {
      return new _VerifyingNode(getHierarchy(), this, pDelegate);
    }
  }

}
