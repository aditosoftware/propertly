package de.verpalnt.propertly.core;

import de.verpalnt.propertly.core.hierarchy.Node;
import de.verpalnt.propertly.core.hierarchy.PPPSupport;
import de.verpalnt.propertly.core.listener.IPropertyEventListener;

import java.util.*;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
public class PropertyPit<S extends IPropertyPitProvider> implements IPropertyPit<S>
{

  private S source;
  private List<IPropertyEventListener> listenerList;
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
  public final synchronized void addPropertyEventListener(IPropertyEventListener pListener)
  {
    if (listenerList == null)
      listenerList = new ArrayList<IPropertyEventListener>();
    listenerList.add(pListener);
  }

  @Override
  public final synchronized void removePropertyEventListener(IPropertyEventListener pListener)
  {
    if (listenerList != null)
    {
      if (listenerList.remove(pListener) && listenerList.isEmpty())
        listenerList = null;
    }
  }

  @Override
  public IPropertyPit<S> getPit()
  {
    return this;
  }

  synchronized List<IPropertyEventListener> getListeners()
  {
    if (listenerList == null)
      return Collections.emptyList();
    return new ArrayList<IPropertyEventListener>(listenerList);
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
