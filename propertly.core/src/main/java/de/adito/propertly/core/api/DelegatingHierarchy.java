package de.adito.propertly.core.api;

import de.adito.propertly.core.common.IFunction;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitEventListener;
import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

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
      public void propertyChanged(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        IProperty prop = _findDelegatingProperty(pProperty);
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          AbstractNode parent = node.getParent();
          if (parent != null)
            parent.fireValueChange(pOldValue, pNewValue); // TODO: translate value to DelegatingNode
          node.fireValueChange(pOldValue, pNewValue); // TODO: translate value to DelegatingNode
        }
      }

      @Override
      public void propertyAdded(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        IProperty prop = _findDelegatingProperty(HierarchyHelper.getNode(pSource).getProperty());
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.fireNodeAdded(prop.getDescription());
        }
      }

      @Override
      public void propertyWillBeRemoved(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        IProperty prop = _findDelegatingProperty(HierarchyHelper.getNode(pSource).getProperty());
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.fireNodeWillBeRemoved(prop.getDescription());
        }
      }

      @Override
      public void propertyRemoved(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        IProperty prop = _findDelegatingProperty(HierarchyHelper.getNode(pSource).getProperty());
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.fireNodeRemoved(prop.getDescription());
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


  public abstract Object delegatingSetValue(INode pDelegateNode, DelegatingNode pDelegatingNode, Object pValue);

  public abstract Object delegatingGetValue(INode pDelegateNode, DelegatingNode pDelegatingNode);

  public abstract boolean canRead(INode pDelegateNode, DelegatingNode pDelegatingNode);

  public abstract boolean canWrite(INode pDelegateNode, DelegatingNode pDelegatingNode);

  public abstract List<INode> delegatingGetChildren(INode pDelegateNode, DelegatingNode pDelegatingNode);

  public abstract INode findDelegatingChild(INode pDelegateNode, DelegatingNode pDelegatingNode, String pName);

  public abstract INode findDelegatingChild(INode pDelegateNode, DelegatingNode pDelegatingNode, IPropertyDescription pPropertyDescription);

  public abstract void delegatingAddProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, IPropertyDescription pPropertyDescription);

  public abstract boolean delegatingRemoveProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, IPropertyDescription pPropertyDescription);

  public abstract void delegatingAddProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, int pIndex, IPropertyDescription pPropertyDescription);

  public abstract void delegatingRemoveProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, int pIndex);

  public abstract void delegatingReorder(INode pDelegateNode, DelegatingNode pDelegatingNode, Comparator pComparator);

  public abstract void rename(INode pDelegateNode, DelegatingNode pDelegatingNode, String pName);


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
