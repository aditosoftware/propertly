package de.verpalnt.propertly.core;

import de.verpalnt.propertly.core.hierarchy.Node;
import de.verpalnt.propertly.core.hierarchy.NodeListener;
import de.verpalnt.propertly.core.listener.IPropertyEventListener;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
public class PropertyPit<S extends IPropertyPitProvider> implements IPropertyPit<S>
{

  private S source;
  private Node node;

  protected PropertyPit()
  {
    source = (S) this;
  }

  private PropertyPit(S pSource)
  {
    source = pSource;
  }

  public static <S extends IPropertyPitProvider> IPropertyPit<S> create(S pCreateFor)
  {
    return new PropertyPit<S>(pCreateFor);
  }

  @Override
  public final IPropertyPitProvider getParent()
  {
    Node parent = node.getParent();
    if (parent == null)
      return null;
    return (IPropertyPitProvider) parent.getProperty().getValue();
  }

  @Override
  public final <SOURCE, T> IProperty<SOURCE, T> findProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    List<Node> children = node.getChildren();
    if (children != null)
      for (Node childNode : children)
      {
        IProperty property = childNode.getProperty();
        if (property.getDescription().equals(pPropertyDescription))
          return property;
      }
    return null;
  }

  @Override
  public final <SOURCE, T> IProperty<SOURCE, T> getProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    IProperty<SOURCE, T> property = findProperty(pPropertyDescription);
    if (property == null)
      throw new RuntimeException("Property for " + pPropertyDescription + " doesn't exist at " + this + ".");
    return property;
  }

  @Override
  public final <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getProperty(pPropertyDescription).getValue();
  }

  @Override
  public final <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue)
  {
    return getProperty(pPropertyDescription).setValue(pValue);
  }

  @Override
  public final Set<IPropertyDescription> getPropertyDescriptions()
  {
    Set<IPropertyDescription> set = new HashSet<IPropertyDescription>();
    for (Node childNode : node.getChildren())
      set.add(childNode.getProperty().getDescription());
    return set;
  }

  @Override
  public List<IProperty> getProperties()
  {
    List<IProperty> properties = new ArrayList<IProperty>();
    for (Node childNode : node.getChildren())
      properties.add(childNode.getProperty());
    return properties;
  }

  @Override
  public final synchronized void addPropertyEventListener(final IPropertyEventListener pListener)
  {
    node.getHierarchy().addNodeListener(new NodeListener()
    {
      @Override
      public void valueChanged(Node pNode, Object pOldValue, Object pNewValue)
      {
        if (node.equals(pNode) || node.equals(pNode.getParent()))
          pListener.propertyChange(pNode.getProperty(), pOldValue, pNewValue);
      }

      @Override
      public void nodeAdded(Node pParent, IPropertyDescription pPropertyDescription)
      {
        if (node.equals(pParent))
          pListener.propertyAdded(pPropertyDescription);
      }

      @Override
      public void nodeRemoved(Node pParent, IPropertyDescription pPropertyDescription)
      {
        if (node.equals(pParent))
          pListener.propertyRemoved(pPropertyDescription);
      }
    });
  }

  @Override
  public final synchronized void removePropertyEventListener(IPropertyEventListener pListener)
  {
    throw new NotImplementedException();
  }

  @Override
  public IPropertyPit<S> getPit()
  {
    return this;
  }

  public void setNode(Node pNode)
  {
    node = pNode;
  }

  public Node getNode()
  {
    return node;
  }
}
