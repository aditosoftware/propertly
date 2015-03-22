package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.api.DelegatingNode;
import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.api.INode;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractForwardDelegatingHierarchy;

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
  public Object delegatingSetValue(INode pDelegateNode, DelegatingNode pDelegatingNode, Object pValue)
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
    return pDelegateNode.setValue(pValue);
  }

  @Override
  public boolean canRead(INode pDelegateNode, DelegatingNode pDelegatingNode)
  {
    AccessModifier mod = pDelegateNode.getProperty().getDescription().getAnnotation(AccessModifier.class);
    return mod == null || mod.canRead();
  }

  @Override
  public boolean canWrite(INode pDelegateNode, DelegatingNode pDelegatingNode)
  {
    AccessModifier mod = pDelegateNode.getProperty().getDescription().getAnnotation(AccessModifier.class);
    return mod == null || mod.canWrite();
  }

}
