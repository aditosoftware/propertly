package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  @NotNull
  public S getSource()
  {
    return source;
  }

  @Override
  public boolean isValid()
  {
    return node != null && node.isValid();
  }

  @NotNull
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

  @NotNull
  @Override
  public IProperty<P, S> getOwnProperty()
  {
    //noinspection unchecked
    return getNode().getProperty();
  }

  @Nullable
  @Override
  public IProperty<S, T> findProperty(@NotNull String pName)
  {
    INode childNode = getNode().findNode(pName);
    //noinspection unchecked
    return childNode == null ? null : childNode.getProperty();
  }

  @Nullable
  @Override
  public <T> IProperty<S, T> findProperty(@NotNull IPropertyDescription<?, T> pPropertyDescription)
  {
    INode childNode = getNode().findNode(pPropertyDescription);
    //noinspection unchecked
    return childNode == null ? null : childNode.getProperty();
  }

  @NotNull
  @Override
  public <E extends T> IProperty<S, E> getProperty(@NotNull IPropertyDescription<? super S, E> pPropertyDescription)
  {
    IProperty<?, E> property = findProperty(pPropertyDescription);
    if (property == null)
      throw new RuntimeException("Property for " + pPropertyDescription + " doesn't exist at " + this + ".");
    //noinspection unchecked
    return (IProperty<S, E>) property;
  }

  @Override
  @Nullable
  public final <E extends T> E getValue(@NotNull IPropertyDescription<? super S, E> pPropertyDescription)
  {
    return getProperty(pPropertyDescription).getValue();
  }

  @Override
  @Nullable
  public final <E extends T> E setValue(@NotNull IPropertyDescription<? super S, E> pPropertyDescription, @Nullable E pValue)
  {
    return getProperty(pPropertyDescription).setValue(pValue);
  }

  @NotNull
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
  @NotNull
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

  @NotNull
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
  public void addWeakListener(@NotNull IPropertyPitEventListener pListener)
  {
    getNode().addWeakListener(pListener);
  }

  @Override
  public void addStrongListener(@NotNull IPropertyPitEventListener pListener)
  {
    getNode().addStrongListener(pListener);
  }

  @Override
  public void removeListener(@NotNull IPropertyPitEventListener pListener)
  {
    getNode().removeListener(pListener);
  }

  @NotNull
  public IPropertyPit<P, S, T> getPit()
  {
    return this;
  }

  void setNode(INode pNode)
  {
    node = pNode;
  }

  @NotNull
  INode getNode()
  {
    if (isValid())
      return node;
    throw new RuntimeException("'" + this + "' for " + source + " has not been initialized, yet.");
  }

}
