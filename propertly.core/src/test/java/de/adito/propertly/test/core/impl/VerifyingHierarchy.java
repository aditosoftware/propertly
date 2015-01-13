package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractForwardDelegatingHierarchy;

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
  public Object delegatingSetValue(INode pDelegateNode, DelegatingNode pDelegatingNode, Object pValue)
  {
    if (pValue instanceof Number)
      for (IntVerifier verifier : PropertlyUtility.findAnnotations(pDelegatingNode.getProperty().getDescription(), IntVerifier.class))
      {
        Number value = (Number) pValue;
        if (value.doubleValue() < verifier.minValue() || value.doubleValue() > verifier.maxValue())
          throw new IllegalArgumentException(value.toString() + " for " + pDelegateNode.getProperty());
      }
    return pDelegateNode.setValue(pValue);
  }

  @Override
  public boolean canRead(INode pDelegateNode, DelegatingNode pDelegatingNode)
  {
    List<AccessModifier> mod = PropertlyUtility.findAnnotations(pDelegateNode.getProperty().getDescription(), AccessModifier.class);
    return mod.isEmpty() || mod.get(0).canRead();
  }

  @Override
  public boolean canWrite(INode pDelegateNode, DelegatingNode pDelegatingNode)
  {
    List<AccessModifier> mod = PropertlyUtility.findAnnotations(pDelegateNode.getProperty().getDescription(), AccessModifier.class);
    return mod.isEmpty() || mod.get(0).canWrite();
  }

}
