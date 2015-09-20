package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.api.DelegatingNode;
import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.api.INode;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractForwardDelegatingHierarchy;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 20:07
 */
public class VerifyingHierarchy<T extends IPropertyPitProvider> extends AbstractForwardDelegatingHierarchy<T>
{

  public VerifyingHierarchy(Hierarchy<T> pHierarchy)
  {
    super(pHierarchy);
  }

  @Override
  public Object delegatingSetValue(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, Object pValue, @Nonnull List<Object> pAttributes)
  {
    if (pValue instanceof Number)
    {
      IntVerifier verifier = pDelegatingNode.getProperty().getDescription().getAnnotation(IntVerifier.class);
      if (verifier != null)
      {
        Number value = (Number) pValue;
        if (value.doubleValue() < verifier.minValue() || value.doubleValue() > verifier.maxValue())
          throw new IllegalArgumentException(value.toString() + " for " + pDelegateNode.getProperty());
      }
    }
    return pDelegateNode.setValue(pValue, pAttributes);
  }

  @Override
  public boolean canRead(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode)
  {
    AccessModifier mod = pDelegateNode.getProperty().getDescription().getAnnotation(AccessModifier.class);
    return mod == null || mod.canRead();
  }

  @Override
  public boolean canWrite(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode)
  {
    AccessModifier mod = pDelegateNode.getProperty().getDescription().getAnnotation(AccessModifier.class);
    return mod == null || mod.canWrite();
  }

}
