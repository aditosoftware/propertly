package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.hierarchy.*;
import de.adito.propertly.core.common.PropertlyUtility;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 20:07
 */
public class VerifyingHierarchy<T extends IPropertyPitProvider> extends DelegatingHierarchy<T>
{

  public VerifyingHierarchy(Hierarchy<T> pHierarchy)
  {
    super(pHierarchy);
  }

  @Override
  public Object delegatingSetValue(INode pDelegateNode, DelegatingNode pDelegatingNode, Object pValue)
  {
    List<? extends Annotation> annotations = pDelegatingNode.getProperty().getDescription().getAnnotations();
    for (Annotation annotation : annotations)
    {
      if (annotation.annotationType().isAssignableFrom(IntVerifier.class) && pValue instanceof Integer)
      {
        IntVerifier verifier = (IntVerifier) annotation;
        Integer value = (Integer) pValue;
        if (value < verifier.minValue() || value > verifier.maxValue())
          throw new IllegalArgumentException(value.toString() + " for " + pDelegateNode.getProperty());
      }
    }
    return pDelegateNode.setValue(pValue);
  }

  @Override
  public Object delegatingGetValue(INode pDelegateNode, DelegatingNode pDelegatingNode)
  {
    return pDelegateNode.getValue();
  }

  @Override
  public List<INode> delegatingGetChildren(INode pDelegateNode, DelegatingNode pDelegatingNode)
  {
    List<INode> children = new ArrayList<INode>();
    List<INode> delegateChildren = pDelegateNode.getChildren();
    if (delegateChildren != null)
    {
      for (final INode delegateChildNode : delegateChildren)
        children.add(new DelegatingNode(this, pDelegatingNode, delegateChildNode.getProperty().getDescription(),
                                        PropertlyUtility.getFixedSupplier(delegateChildNode)));
    }
    return children;
  }

  @Override
  public INode findDelegatingChild(INode pDelegateNode, DelegatingNode pDelegatingNode, IPropertyDescription pPropertyDescription)
  {
    INode delegateChildNode = pDelegateNode.findNode(pPropertyDescription);
    if (delegateChildNode == null)
      return null;
    return new DelegatingNode(this, pDelegatingNode, delegateChildNode.getProperty().getDescription(),
                              PropertlyUtility.getFixedSupplier(delegateChildNode));
  }

  @Override
  public void delegatingAddProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, IPropertyDescription pPropertyDescription)
  {
    pDelegateNode.addProperty(pPropertyDescription);
  }

  @Override
  public boolean delegatingRemoveProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, IPropertyDescription pPropertyDescription)
  {
    return pDelegateNode.removeProperty(pPropertyDescription);
  }

  @Override
  public void delegatingAddProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, int pIndex, IPropertyDescription pPropertyDescription)
  {
    pDelegateNode.addProperty(pIndex, pPropertyDescription);
  }

  @Override
  public void delegatingRemoveProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, int pindex)
  {
    pDelegateNode.removeProperty(pindex);
  }

  @Override
  public void delegatingReorder(INode pDelegateNode, DelegatingNode pDelegatingNode, Comparator pComparator)
  {
    pDelegateNode.reorder(pComparator);
  }

  @Override
  public void rename(INode pDelegateNode, DelegatingNode pDelegatingNode, String pName)
  {
    pDelegateNode.rename(pName);
  }
}
