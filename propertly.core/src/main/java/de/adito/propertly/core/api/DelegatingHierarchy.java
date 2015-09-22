package de.adito.propertly.core.api;

import de.adito.propertly.core.common.*;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 23:00
 */
public abstract class DelegatingHierarchy<T extends IPropertyPitProvider> extends Hierarchy<T>
{

  @SuppressWarnings("FieldCanBeLocal")
  private IPropertyPitEventListener propertyPitEventListener; // strong reference for weak listener

  protected DelegatingHierarchy(Hierarchy<T> pSourceHierarchy)
  {
    super(new _INodeFunction(pSourceHierarchy), pSourceHierarchy.getValue());

    propertyPitEventListener = new IPropertyPitEventListener()
    {
      @Override
      public void propertyChanged(@Nonnull IProperty pProperty, @Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Set pAttributes)
      {
        IProperty prop = _findDelegatingProperty(pProperty);
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          AbstractNode parent = node.getParent();
          if (parent != null)
            parent.fireValueChange(pOldValue, pNewValue, pAttributes); // TODO: translate value to DelegatingNode
          node.fireValueChange(pOldValue, pNewValue, pAttributes); // TODO: translate value to DelegatingNode
        }
      }

      @Override
      public void propertyAdded(@Nonnull IPropertyPitProvider pSource, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set pAttributes)
      {
        IProperty prop = _findDelegatingProperty(pSource.getPit().getOwnProperty());
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.fireNodeAdded(prop.getDescription(), pAttributes);
        }
      }

      @Override
      public void propertyWillBeRemoved(@Nonnull IPropertyPitProvider pSource, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set pAttributes)
      {
        IProperty prop = _findDelegatingProperty(pSource.getPit().getOwnProperty());
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.fireNodeWillBeRemoved(prop.getDescription(), pAttributes);
        }
      }

      @Override
      public void propertyRemoved(@Nonnull IPropertyPitProvider pSource, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set pAttributes)
      {
        IProperty prop = _findDelegatingProperty(pSource.getPit().getOwnProperty());
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.fireNodeRemoved(prop.getDescription(), pAttributes);
        }
      }

      @Override
      public void propertyNameChanged(@Nonnull IProperty pProperty, @Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set pAttributes)
      {
        IProperty prop = _findDelegatingProperty(pProperty);
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.firePropertyNameChanged(pOldName, pNewName, pAttributes);
        }
      }

      @Override
      public void propertyOrderChanged(@Nonnull IPropertyPitProvider pSource, @Nonnull Set pAttributes)
      {
        IProperty prop = _findDelegatingProperty(pSource.getPit().getOwnProperty());
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.firePropertyOrderChanged(pAttributes);
        }
      }
    };

    pSourceHierarchy.addWeakListener(propertyPitEventListener);
  }

  @Nullable
  private IProperty _findDelegatingProperty(IProperty pProperty)
  {
    if (pProperty instanceof HierarchyProperty)
    {
      INode delegatingNode = _findDelegatingNode(((HierarchyProperty) pProperty).getNode());
      if (delegatingNode != null)
        return delegatingNode.getProperty();
    }
    return null;
  }

  @Nullable
  private INode _findDelegatingNode(INode pNode)
  {
    INode parent = pNode.getParent();
    if (parent == null)
      return getNode();

    INode delegatingNode = _findDelegatingNode(parent);
    return delegatingNode != null ? delegatingNode.findNode(pNode.getProperty().getDescription()) : null;
  }


  public abstract Object delegatingSetValue(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nullable Object pValue, @Nonnull Set<Object> pAttributes);

  public abstract Object delegatingGetValue(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode);

  public abstract boolean canRead(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode);

  public abstract boolean canWrite(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode);

  public abstract List<INode> delegatingGetChildren(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode);

  public abstract INode findDelegatingChild(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull String pName);

  public abstract INode findDelegatingChild(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull IPropertyDescription pPropertyDescription);

  public abstract void delegatingAddProperty(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes);

  public abstract boolean delegatingRemoveProperty(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes);

  public abstract void delegatingAddProperty(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, int pIndex, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes);

  public abstract void delegatingRemoveProperty(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, int pIndex, @Nonnull Set<Object> pAttributes);

  public abstract void delegatingReorder(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull Comparator pComparator, @Nonnull Set<Object> pAttributes);

  public abstract void rename(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull String pName, @Nonnull Set<Object> pAttributes);

  public abstract int delegatingIndexOf(@Nonnull INode pDelegateNode, @Nonnull DelegatingNode pDelegatingNode, @Nonnull IPropertyDescription pPropertyDescription);


  /**
   * IFunction implementation
   */
  private static class _INodeFunction implements IFunction<Hierarchy, INode>
  {
    Hierarchy sourceHierarchy;

    _INodeFunction(Hierarchy pSourceHierarchy)
    {
      sourceHierarchy = pSourceHierarchy;
    }

    @Override
    public INode run(Hierarchy pHierarchy)
    {
      INode sourceNode = sourceHierarchy.getNode();
      return new DelegatingNode((DelegatingHierarchy) pHierarchy, null, sourceNode.getProperty().getDescription(),
                                PropertlyUtility.getFixedSupplier(sourceNode));
    }
  }

}
