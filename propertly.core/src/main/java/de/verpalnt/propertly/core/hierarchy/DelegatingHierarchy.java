package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.IFunction;
import de.verpalnt.propertly.core.common.ISupplier;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 23:00
 */
public abstract class DelegatingHierarchy<T extends IPropertyPitProvider> extends Hierarchy<T>
{

  protected DelegatingHierarchy(final Hierarchy<T> pSourceHierarchy)
  {
    super(new IFunction<Hierarchy, INode>()
    {
      @Override
      public INode run(Hierarchy pHierarchy)
      {
        final INode sourceNode = pSourceHierarchy.getNode();
        return new DelegatingNode((DelegatingHierarchy) pHierarchy, null, sourceNode.getProperty(), new ISupplier<INode>()
        {
          @Override
          public INode get()
          {
            return sourceNode;
          }
        });
      }
    }, pSourceHierarchy.getValue());

    pSourceHierarchy.addPropertyEventListener(new IPropertyEventListener()
    {
      @Override
      public void propertyChange(IProperty pProperty, Object pOldValue, Object pNewValue)
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
    });
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
    if (delegatingNode != null)
    {
      List<INode> children = delegatingNode.getChildren();
      if (children != null)
      {
        String searchedNodeName = pNode.getProperty().getName();
        for (INode node : children)
          if (searchedNodeName.equals(node.getProperty().getName()))
            return node;
      }
    }
    return null;
  }


  public abstract Object delegatingSetValue(INode pDelegateNode, DelegatingNode pDelegatingNode, Object pValue);

  public abstract Object delegatingGetValue(INode pDelegateNode, DelegatingNode pDelegatingNode);

  public abstract List<INode> delegatingGetChildren(INode pDelegateNode, DelegatingNode pDelegatingNode);

  public abstract void delegatingAddProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, IPropertyDescription pPropertyDescription);

  public abstract boolean delegatingRemoveProperty(INode pDelegateNode, DelegatingNode pDelegatingNode, String pName);

}
