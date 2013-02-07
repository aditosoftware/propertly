package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;
import de.verpalnt.propertly.core.common.PPPIntrospector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Node
{

  private final Hierarchy hierarchy;
  private final Node parent;
  private HierarchyProperty property;

  private Object value;
  private List<Node> children;
  private List<IPropertyEventListener> listeners;


  protected Node(@Nonnull Hierarchy pHierarchy, @Nullable Node pParent, @Nonnull IPropertyDescription pPropertyDescription)
  {
    hierarchy = pHierarchy;
    parent = pParent;
    property = new HierarchyProperty(this, pPropertyDescription);
  }

  protected Object setValue(Object pValue)
  {
    if ((value == pValue) || (value != null && value.equals(pValue)))
      return value; // nothing changes with equal values.

    if (pValue != null)
    {
      Class type = property.getType();
      if (!type.isAssignableFrom(pValue.getClass()))
        throw new IllegalArgumentException("'" + pValue + "' can't be set for field with type '" + type + "'.");
    }
    Object oldValue = value;
    IPropertyPitProvider pppProvider = null;
    if (pValue instanceof IPropertyPitProvider)
      pppProvider = (IPropertyPitProvider) pValue;
    if (pppProvider != null && pppProvider.getPit().getNode() != null && hierarchy.equals(pppProvider.getPit().getNode().hierarchy))
      throw new IllegalStateException("can't set PPP from my own hierarchy.");
    if (oldValue instanceof IPropertyPitProvider)
    {
      for (Node child : children)
        child.setValue(null);
      ((IPropertyPitProvider) oldValue).getPit().setNode(null);
    }
    if (pppProvider != null)
    {
      IPropertyPitProvider pppCopy;
      try
      {
        pppCopy = pppProvider.getClass().newInstance();
      }
      catch (Exception e)
      {
        throw new RuntimeException("can't instantiate: " + pppProvider);
      }
      value = pppCopy;
      pppCopy.getPit().setNode(this);
      Node node = pppProvider.getPit().getNode();

      if (node == null)
      {
        Set<IPropertyDescription> descriptions = PPPIntrospector.get(pppProvider.getClass());
        List<Node> nodes = new ArrayList<Node>(descriptions.size());
        for (IPropertyDescription description : descriptions)
          nodes.add(new Node(hierarchy, this, description));
        children = nodes;
      }
      else
      {
        List<Node> childNodes = node.getChildren();
        assert childNodes != null;
        List<Node> newNodes = new ArrayList<Node>(childNodes.size());
        for (Node child : childNodes)
          newNodes.add(new Node(hierarchy, this, child.getProperty().getDescription()));
        children = newNodes;
        for (int i = 0; i < childNodes.size(); i++)
          newNodes.get(i).setValue(childNodes.get(i));
      }
    }
    else
    {
      value = pValue;
      children = null;
    }
    _fireValueChange(oldValue, pValue);
    return value;
  }

  protected Object getValue()
  {
    return value;
  }

  protected Hierarchy getHierarchy()
  {
    return hierarchy;
  }

  protected Node getParent()
  {
    return parent;
  }

  protected String getPath()
  {
    Node parentNode = getParent();
    String name = getProperty().getName();
    return parentNode == null ? name : parentNode.getPath() + "/" + name;
  }

  @Nullable
  protected List<Node> getChildren()
  {
    return children;
  }

  protected IProperty getProperty()
  {
    return property;
  }

  protected IPropertyDescription getPropertyDescription()
  {
    return property.getDescription();
  }

  protected boolean isLeaf()
  {
    return IPropertyPitProvider.class.isAssignableFrom(property.getType());
  }

  protected void addProperty(IPropertyDescription pPropertyDescription)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + property);
    Node node = _find(pPropertyDescription.getName());
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);
    children.add(new Node(hierarchy, this, pPropertyDescription));
    _fireNodeAdded(pPropertyDescription);
  }

  protected boolean removeProperty(String pName)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + property);
    Node node = _find(pName);
    if (node != null)
    {
      IProperty nProp = node.getProperty();
      if (IMutablePropertyPitProvider.class.isAssignableFrom(nProp.getType()))
      {
        IPropertyDescription descr = nProp.getDescription();
        children.remove(node);
        _fireNodeRemoved(descr);
        return true;
      }
    }
    return false;
  }

  protected void addPropertyEventListener(IPropertyEventListener pListener)
  {
    if (listeners == null)
      listeners = new ArrayList<IPropertyEventListener>();
    listeners.add(pListener);
  }

  protected void removePropertyEventListener(IPropertyEventListener pListener)
  {
    if (listeners != null)
      listeners.add(pListener);
  }

  private void _fireValueChange(Object pOldValue, Object pNewValue)
  {
    hierarchy.fireNodeChanged(getProperty(), pOldValue, pNewValue);
    Node p = getParent();
    if (p != null)
    {
      List<IPropertyEventListener> l = p.listeners;
      if (l != null)
        for (IPropertyEventListener eventListener : l)
          eventListener.propertyChange(getProperty(), pOldValue, pNewValue);
    }
    property.fire(pOldValue, pNewValue);
  }

  private void _fireNodeAdded(IPropertyDescription pPropertyDescription)
  {
    hierarchy.firePropertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyEventListener listener : listeners)
        listener.propertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  private void _fireNodeRemoved(IPropertyDescription pPropertyDescription)
  {
    hierarchy.firePropertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyEventListener listener : listeners)
        listener.propertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  private Node _find(String pName)
  {
    if (children != null)
      for (Node child : children)
        if (pName.equals(child.getProperty().getName()))
          return child;
    return null;
  }

}
