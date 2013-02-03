package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.IMutablePropertyPitProvider;
import de.verpalnt.propertly.core.IProperty;
import de.verpalnt.propertly.core.IPropertyDescription;
import de.verpalnt.propertly.core.IPropertyPitProvider;
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
  private IProperty property;

  private Object value;
  private List<Node> children;
  private List<NodeListener> listeners;


  public Node(@Nonnull Hierarchy pHierarchy, @Nullable Node pParent, @Nonnull IPropertyDescription pPropertyDescription)
  {
    hierarchy = pHierarchy;
    parent = pParent;
    if (IMutablePropertyPitProvider.class.isAssignableFrom(pPropertyDescription.getType()))
      property = new MutableHierarchyProperty(this, pPropertyDescription);
    else
      property = new HierarchyProperty(this, pPropertyDescription);
  }

  public Object setValue(Object pValue)
  {
    if ((value == pValue) || (value != null && value.equals(pValue)))
      return value; // nothing changes with equal values.

    if (pValue != null)
    {
      Class type = property.getDescription().getType();
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
    hierarchy.fireNodeChanged(this, oldValue, pValue);
    return value;
  }

  public Object getValue()
  {
    return value;
  }

  public Hierarchy getHierarchy()
  {
    return hierarchy;
  }

  public Node getParent()
  {
    return parent;
  }

  public String getPath()
  {
    Node parentNode = getParent();
    String name = getProperty().getDescription().getName();
    return parentNode == null ? name : parentNode.getPath() + "/" + name;
  }

  @Nullable
  public List<Node> getChildren()
  {
    return children;
  }

  @Nullable
  public PPPSupport getPPPSupport()
  {
    return value instanceof PPPSupport ? (PPPSupport) value : null;
  }

  @Nullable
  public IPropertyPitProvider getPropertyPitProvider()
  {
    PPPSupport pppSupport = getPPPSupport();
    return pppSupport == null ? null : pppSupport.getPPP();
  }

  public IProperty getProperty()
  {
    return property;
  }

  public boolean isLeaf()
  {
    return IPropertyPitProvider.class.isAssignableFrom(property.getDescription().getType());
  }

  public void addProperty(IPropertyDescription pPropertyDescription)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + property);
    Node node = _find(pPropertyDescription.getName());
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);
    children.add(new Node(hierarchy, this, pPropertyDescription));
    hierarchy.fireNodeAdded(this, pPropertyDescription);
  }

  public void removeProperty(String pName)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + property);
    Node node = _find(pName);
    if (node != null)
    {
      IProperty nProp = node.getProperty();
      if (nProp instanceof MutableHierarchyProperty)
      {
        IPropertyDescription descr = nProp.getDescription();
        children.remove(node);
        hierarchy.fireNodeRemoved(this, descr);
      }
    }
  }

  private Node _find(String pName)
  {
    if (children != null)
      for (Node child : children)
        if (pName.equals(child.getProperty().getDescription().getName()))
          return child;
    return null;
  }

}
