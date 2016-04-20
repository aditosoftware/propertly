package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 03.10.11
 *         Time: 22:02
 */
class PropertyPit<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    implements IPropertyPit<P, S, T>
{

  private S source;
  private INode node;


  PropertyPit(S pSource)
  {
    source = pSource;
  }

  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T> PropertyPit<P, S, T> create(S pCreateFor)
  {
    return new PropertyPit<>(pCreateFor);
  }

  @Override
  @Nonnull
  public S getSource()
  {
    return source;
  }

  @Override
  public boolean isValid()
  {
    return node != null;
  }

  @Nonnull
  @Override
  public IHierarchy<?> getHierarchy()
  {
    return getNode().getHierarchy();
  }

  @Override
  @Nullable
  public final P getParent()
  {
    INode parent = getNode().getParent();
    //noinspection unchecked
    return parent == null ? null : (P) parent.getProperty().getValue();
  }

  @Nonnull
  @Override
  public IProperty<P, S> getOwnProperty()
  {
    //noinspection unchecked
    return getNode().getProperty();
  }

  @Nullable
  @Override
  public IProperty<S, T> findProperty(@Nonnull String pName)
  {
    INode childNode = getNode().findNode(pName);
    //noinspection unchecked
    return childNode == null ? null : childNode.getProperty();
  }

  @Nullable
  @Override
  public <T> IProperty<S, T> findProperty(@Nonnull IPropertyDescription<?, T> pPropertyDescription)
  {
    INode childNode = getNode().findNode(pPropertyDescription);
    //noinspection unchecked
    return childNode == null ? null : childNode.getProperty();
  }

  @Nonnull
  @Override
  public <E extends T> IProperty<S, E> getProperty(@Nonnull IPropertyDescription<? super S, E> pPropertyDescription)
  {
    IProperty<?, E> property = findProperty(pPropertyDescription);
    if (property == null)
      throw new RuntimeException("Property for " + pPropertyDescription + " doesn't exist at " + this + ".");
    //noinspection unchecked
    return (IProperty<S, E>) property;
  }

  @Override
  @Nullable
  public final <E extends T> E getValue(@Nonnull IPropertyDescription<? super S, E> pPropertyDescription)
  {
    return getProperty(pPropertyDescription).getValue();
  }

  @Override
  @Nullable
  public final <E extends T> E setValue(@Nonnull IPropertyDescription<? super S, E> pPropertyDescription, @Nullable E pValue)
  {
    return getProperty(pPropertyDescription).setValue(pValue);
  }

  @Nonnull
  @Override
  public final Set<IPropertyDescription<S, T>> getPropertyDescriptions()
  {
    Set<IPropertyDescription<S, T>> set = new LinkedHashSet<>();
    List<INode> children = getNode().getChildren();
    if (children != null)
      for (INode childNode : children)
        //noinspection unchecked
        set.add(childNode.getProperty().getDescription());
    return set;
  }

  @Override
  @Nonnull
  public List<IProperty<S, T>> getProperties()
  {
    List<IProperty<S, T>> properties = new ArrayList<>();
    List<INode> children = getNode().getChildren();
    if (children != null)
      for (INode childNode : children)
        //noinspection unchecked
        properties.add(childNode.getProperty());
    return properties;
  }

  @Nonnull
  @Override
  public List<T> getValues()
  {
    List<T> values = new ArrayList<>();
    List<INode> children = getNode().getChildren();
    if (children != null)
    {
      for (INode childNode : children)
      {
        //noinspection unchecked
        T value = (T) childNode.getValue();
        if (value != null)
          values.add(value);
      }
    }
    return values;
  }

  @Override
  public Class<T> getChildType()
  {
    return (Class<T>) Object.class;
  }

  @Override
  public Iterator<IProperty<S, T>> iterator()
  {
    return getProperties().iterator();
  }

  @Override
  public void addWeakListener(@Nonnull IPropertyPitEventListener pListener)
  {
    getNode().addWeakListener(pListener);
  }

  @Override
  public void addStrongListener(@Nonnull IPropertyPitEventListener pListener)
  {
    getNode().addStrongListener(pListener);
  }

  @Override
  public void removeListener(@Nonnull IPropertyPitEventListener pListener)
  {
    getNode().removeListener(pListener);
  }

  @Nonnull
  public IPropertyPit<P, S, T> getPit()
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
    if (isValid())
      return node;
    throw new RuntimeException("'" + this + "' for " + source + " has not been initialized, yet.");
  }

}
