package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
public class PropertyPit<S extends IPropertyPitProvider> implements IPropertyPit<S>
{

  private S source;
  private INode node;


  PropertyPit(S pSource)
  {
    source = pSource;
  }

  public static <S extends IPropertyPitProvider> PropertyPit<S> create(S pCreateFor)
  {
    return new PropertyPit<S>(pCreateFor);
  }

  @Override
  @Nonnull
  public S getSource()
  {
    return source;
  }

  @Override
  @Nullable
  public final IPropertyPitProvider<?> getParent()
  {
    INode parent = getNode().getParent();
    return parent == null ? null : (IPropertyPitProvider) parent.getProperty().getValue();
  }

  @Nonnull
  @Override
  public IProperty<?, S> getOwnProperty()
  {
    return getNode().getProperty();
  }

  @Nullable
  @Override
  public <T> IProperty<S, T> findProperty(IPropertyDescription<?, T> pPropertyDescription)
  {
    INode childNode = getNode().findNode(pPropertyDescription);
    //noinspection unchecked
    return childNode == null ? null : childNode.getProperty();
  }

  @Nonnull
  @Override
  public <T> IProperty<S, T> getProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    IProperty<?, T> property = findProperty(pPropertyDescription);
    if (property == null)
      throw new RuntimeException("Property for " + pPropertyDescription + " doesn't exist at " + this + ".");
    //noinspection unchecked
    return (IProperty<S, T>) property;
  }

  @Override
  @Nullable
  public final <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getProperty(pPropertyDescription).getValue();
  }

  @Override
  @Nullable
  public final <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue)
  {
    return getProperty(pPropertyDescription).setValue(pValue);
  }

  @Override
  public final Set<IPropertyDescription> getPropertyDescriptions()
  {
    Set<IPropertyDescription> set = new LinkedHashSet<IPropertyDescription>();
    List<INode> children = getNode().getChildren();
    if (children != null)
      for (INode childNode : children)
        set.add(childNode.getProperty().getDescription());
    return set;
  }

  @Override
  @Nonnull
  public List<IProperty<S, ?>> getProperties()
  {
    List<IProperty<S, ?>> properties = new ArrayList<IProperty<S, ?>>();
    List<INode> children = getNode().getChildren();
    if (children != null)
      for (INode childNode : children)
        properties.add(childNode.getProperty());
    return properties;
  }

  @Override
  public Iterator<IProperty<S, ?>> iterator()
  {
    return getProperties().iterator();
  }

  @Override
  public final void addPropertyEventListener(final IPropertyEventListener pListener)
  {
    getNode().addPropertyEventListener(pListener);
  }

  @Override
  public final void removePropertyEventListener(IPropertyEventListener pListener)
  {
    getNode().removePropertyEventListener(pListener);
  }

  public IPropertyPit<S> getPit()
  {
    return this;
  }

  void setNode(INode pNode)
  {
    node = pNode;
  }

  @Nonnull
  INode getNode()
  {
    if (node == null)
      throw new RuntimeException(this + " for " + source + " has not been initialized, yet.");
    return node;
  }

  boolean hasNode()
  {
    return node != null;
  }
}
