package de.adito.propertly.core.spi.extension;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Simple DelegatingHierarchy which forwards all calls to the delegated node.
 *
 * @author j.boesl, 13.01.15
 */
public class AbstractForwardDelegatingHierarchy<T extends IPropertyPitProvider> extends DelegatingHierarchy<T>
{

  private Map<INode, INode> delegateMap = new WeakHashMap<>();


  protected AbstractForwardDelegatingHierarchy(Hierarchy<T> pHierarchy)
  {
    super(pHierarchy);
  }

  @Override
  public Object delegatingSetValue(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, Object pValue, @Nonnull Set<Object> pAttributes)
  {
    return pDelegateNode.setValue(pValue, pAttributes);
  }

  @Override
  public Object delegatingGetValue(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode)
  {
    return pDelegateNode.getValue();
  }

  @Override
  public boolean canRead(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode)
  {
    return pDelegateNode.canRead();
  }

  @Override
  public boolean canWrite(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode)
  {
    return pDelegateNode.canWrite();
  }

  @Override
  public List<INode> delegatingGetChildren(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode)
  {
    List<INode> children = new ArrayList<>();
    List<INode> delegateChildren = pDelegateNode.getChildren();
    if (delegateChildren != null)
      for (final INode delegateChildNode : delegateChildren)
        children.add(getDelegatingNode(delegateChildNode, pDelegatingNode));
    return children;
  }

  @Override
  public INode findDelegatingChild(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull String pName)
  {
    INode delegateChildNode = pDelegateNode.findNode(pName);
    if (delegateChildNode == null)
      return null;
    return getDelegatingNode(delegateChildNode, pDelegatingNode);
  }

  @Override
  public INode findDelegatingChild(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull IPropertyDescription pPropertyDescription)
  {
    INode delegateChildNode = pDelegateNode.findNode(pPropertyDescription);
    if (delegateChildNode == null)
      return null;
    return new DelegatingNode(this, pDelegatingNode, delegateChildNode.getProperty().getDescription(),
                              PropertlyUtility.getFixedSupplier(delegateChildNode));
  }

  @Override
  public void delegatingAddProperty(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    pDelegateNode.addProperty(pPropertyDescription, pAttributes);
  }

  @Override
  public boolean delegatingRemoveProperty(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    return pDelegateNode.removeProperty(pPropertyDescription, pAttributes);
  }

  @Override
  public void delegatingAddProperty(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, int pIndex, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    pDelegateNode.addProperty(pIndex, pPropertyDescription, pAttributes);
  }

  @Override
  public void delegatingRemoveProperty(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, int pindex, @Nonnull Set<Object> pAttributes)
  {
    pDelegateNode.removeProperty(pindex, pAttributes);
  }

  @Override
  public void delegatingReorder(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull Comparator pComparator, @Nonnull Set<Object> pAttributes)
  {
    pDelegateNode.reorder(pComparator, pAttributes);
  }

  @Override
  public void rename(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull String pName, @Nonnull Set<Object> pAttributes)
  {
    pDelegateNode.rename(pName, pAttributes);
  }

  @Override
  public int delegatingIndexOf(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull IPropertyDescription pPropertyDescription)
  {
    return pDelegateNode.indexOf(pPropertyDescription);
  }

  protected INode getDelegatingNode(INode pDelegateChildNode, DelegatingNode pDelegatingParent)
  {
    INode node = delegateMap.get(pDelegateChildNode);
    if (node == null)
    {
      node = new DelegatingNode(this, pDelegatingParent, pDelegateChildNode.getProperty().getDescription(),
                                PropertlyUtility.getFixedSupplier(pDelegateChildNode));
      delegateMap.put(pDelegateChildNode, node);
    }
    return node;
  }
}
