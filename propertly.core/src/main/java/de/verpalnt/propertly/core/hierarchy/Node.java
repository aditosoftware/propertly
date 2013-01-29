package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.IProperty;
import de.verpalnt.propertly.core.IPropertyDescription;
import de.verpalnt.propertly.core.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.PPPIntrospector;

import java.util.ArrayList;
import java.util.List;

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


  public Node(Hierarchy pHierarchy, Node pParent, IPropertyDescription pPropertyDescription)
  {
    hierarchy = pHierarchy;
    parent = pParent;
    property = new HierarchyProperty(this, pPropertyDescription);
  }

  public void setValue(Object pValue)
  {
    Object oldValue = value;
    value = pValue;
    if (pValue instanceof Class && IPropertyPitProvider.class.isAssignableFrom((Class<?>) pValue))
    {
      //noinspection unchecked
      List<IPropertyDescription> descriptions = PPPIntrospector.get((Class<? extends IPropertyPitProvider>) pValue);
      List<Node> nodes = new ArrayList<Node>(descriptions.size());
      for (IPropertyDescription description : descriptions)
        nodes.add(new Node(hierarchy, this, description));
      children = nodes;
    }
    else
      children = null;
    property = null;
    hierarchy.fireNodeChanged(this, oldValue, pValue);
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

  public List<Node> getChildren()
  {
    return children;
  }

  public IProperty getProperty()
  {
    return property;
  }

  public boolean isLeaf()
  {
    return children == null;
  }

}
