package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.ISupplier;

import java.util.List;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 23:00
 */
public abstract class DelegatingHierarchy<T extends IPropertyPitProvider> extends Hierarchy<T>
{

  protected DelegatingHierarchy(Hierarchy<T> pHierarchy)
  {
    super(pHierarchy.getProperty().getName(), pHierarchy.getValue(), pHierarchy);
    pHierarchy.addPropertyEventListener(new IPropertyEventListener()
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
        IProperty prop = _findDelegatingProperty(pSource.getPit().getNode().getProperty());
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.fireNodeAdded(prop.getDescription());
        }
      }

      @Override
      public void propertyRemoved(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        IProperty prop = _findDelegatingProperty(pSource.getPit().getNode().getProperty());
        if (prop != null)
        {
          AbstractNode node = ((HierarchyProperty) prop).getNode();
          node.fireNodeRemoved(prop.getDescription());
        }
      }
    });
  }

  private IProperty _findDelegatingProperty(IProperty pProperty)
  {
    IPropertyPitProvider parent = pProperty.getParent();
    if (parent == null)
      return getNode().getProperty();
    HierarchyProperty delegatingProperty = (HierarchyProperty) _findDelegatingProperty(parent.getPit().getNode().getProperty());
    if (delegatingProperty != null)
    {
      AbstractNode node = delegatingProperty.getNode().find(pProperty.getName());
      if (node != null)
        return node.getProperty();
    }
    return null;
  }

  @Override
  protected final AbstractNode createNode(String pName, Object pExtra)
  {
    Hierarchy<T> sourceHierarchy = (Hierarchy<T>) pExtra;
    final AbstractNode sourceNode = sourceHierarchy.getNode();
    return new DelegatingNode(this, null, sourceNode.getProperty(), new ISupplier<AbstractNode>()
    {
      @Override
      public AbstractNode get()
      {
        return sourceNode;
      }
    });
  }


  public abstract Object delegatingSetValue(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode, Object pValue);

  public abstract Object delegatingGetValue(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode);

  public abstract List<AbstractNode> delegatingGetChildren(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode);

  public abstract void delegatingAddProperty(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode, IPropertyDescription pPropertyDescription);

  public abstract boolean delegatingRemoveProperty(AbstractNode pDelegateNode, DelegatingNode pDelegatingNode, String pName);

}
