package de.adito.propertly.core.api;

import de.adito.propertly.core.common.ListenerList;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract class for INode implementations.
 *
 * @author PaL
 *         Date: 09.02.13
 *         Time: 19:36
 */
public abstract class AbstractNode implements INode
{

  private Hierarchy hierarchy;
  private AbstractNode parent;
  private HierarchyProperty property;

  private ListenerList<IPropertyPitEventListener> listeners;


  protected AbstractNode(@Nonnull Hierarchy pHierarchy, @Nullable AbstractNode pParent,
                         @Nonnull IPropertyDescription pPropertyDescription)
  {
    hierarchy = pHierarchy;
    parent = pParent;
    if (parent == null || parent.getValue() instanceof IMutablePropertyPitProvider)
      property = new DynamicHierarchyProperty(this, pPropertyDescription);
    else
      property = new HierarchyProperty(this, pPropertyDescription);
    listeners = new ListenerList<>();
  }

  @Nonnull
  @Override
  public Hierarchy getHierarchy()
  {
    ensureValid();
    return hierarchy;
  }

  @Nullable
  @Override
  public AbstractNode getParent()
  {
    return parent;
  }

  @Nullable
  @Override
  public Object setValue(@Nullable Object pValue, @Nonnull Set<Object> pAttributes)
  {
    Object value = getValue();
    if (value == pValue || value != null && value.equals(pValue))
      return value; // nothing changes with equal values.

    if (pValue != null)
    {
      Class type = getProperty().getType();
      //noinspection unchecked
      if (!type.isAssignableFrom(pValue.getClass()))
        throw new IllegalArgumentException("'" + pValue + "' can't be set for field with type '" + type + "'.");
    }

    IPropertyPitProvider pppProvider;
    if (pValue instanceof IPropertyPitProvider)
    {
      pppProvider = (IPropertyPitProvider) pValue;

      if (pppProvider.getPit().isValid() && getHierarchy().equals(HierarchyHelper.getNode(pppProvider).getHierarchy()))
      {
        PropertyPath path = new PropertyPath(getProperty());
        PropertyPath pppPath = new PropertyPath(pppProvider);
        if (path.equals(pppPath))
          return pppProvider;
        if (path.isParentOf(pppPath) || pppPath.isParentOf(path))
        {
          String message = MessageFormat.format(
              "The new value for a property mustn't be the parent or a child (set ''{0}'' to ''{1}'').", pppPath, path);
          throw new IllegalStateException(message);
        }
      }
    }
    else
      pppProvider = null;

    changeValue(pValue, () -> {
      if (pppProvider == null)
        updateValue(pValue, pAttributes);
      else
      {
        getChildrenStream().ifPresent(stream ->
            stream.collect(Collectors.toList()).stream().forEach(this::removeChild));
        updateValue(pppProvider, pAttributes);
      }
    }, pAttributes);
    return getValue();
  }

  protected abstract void updateValue(@Nonnull IPropertyPitProvider pValue, @Nonnull Set<Object> pAttributes);

  protected abstract void updateValue(@Nullable Object pValue, @Nonnull Set<Object> pAttributes);

  @Nonnull
  @Override
  public abstract Optional<? extends Stream<AbstractNode>> getChildrenStream();

  @Nonnull
  @Override
  public IProperty getProperty()
  {
    ensureValid();
    return property;
  }

  @Override
  public boolean canRead()
  {
    return isValid();
  }

  @Override
  public boolean canWrite()
  {
    return isValid();
  }

  @Override
  public boolean isValid()
  {
    return hierarchy != null;
  }

  protected abstract void removeChild(AbstractNode pNode);

  @Override
  public void remove(@Nonnull Set<Object> pAttributes)
  {
    IProperty property = getProperty();
    if (property.isDynamic())
    {
      AbstractNode parent = getParent();
      if (parent != null)
      {
        parent.removeProperty(property, () -> {
          getChildrenStream().ifPresent(stream ->
              stream.forEach(node -> node.remove(pAttributes)));
          parent.removeChild(this);
          invalidate(pAttributes);
        }, pAttributes);
        return;
      }
    }
    invalidate(pAttributes);
  }

  protected void invalidate(@Nonnull Set<Object> pAttributes)
  {
    hierarchy = null;
    parent = null;
    property = null;
    listeners = null;
  }

  @Override
  public void addWeakListener(@Nonnull IPropertyPitEventListener pListener)
  {
    ensureValid();
    listeners.addWeakListener(pListener);
  }

  @Override
  public void addStrongListener(@Nonnull IPropertyPitEventListener pListener)
  {
    ensureValid();
    listeners.addStrongListener(pListener);
  }

  @Override
  public void removeListener(@Nonnull IPropertyPitEventListener pListener)
  {
    ensureValid();
    listeners.removeListener(pListener);
  }

  protected Object changeValue(@Nullable Object pValue, @Nonnull Runnable pApplyChange, @Nonnull Set<Object> pAttributes)
  {
    Object oldValue = getValue();
    ListenerList<Runnable> onFinish = new ListenerList<>();
    fireValueWillBeChanged(oldValue, pValue, onFinish::addStrongListener, pAttributes);
    pApplyChange.run();
    Object newValue = getValue();
    fireValueChanged(oldValue, newValue, pAttributes);
    onFinish.forEach(Runnable::run);
    return newValue;
  }

  protected void fireValueWillBeChanged(@Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Consumer<Runnable> pOnRemoved,
                                        @Nonnull Set<Object> pAttributes)
  {
    IProperty localProperty = getProperty();
    getHierarchy().fireValueWillBeChanged(localProperty, pOldValue, pNewValue, pOnRemoved, pAttributes);
    AbstractNode localParent = getParent();
    if (localParent != null)
    {
      for (IPropertyPitEventListener eventListener : localParent.listeners)
        //noinspection unchecked
        eventListener.propertyValueWillBeChanged(localProperty, pOldValue, pNewValue, pOnRemoved, pAttributes);
    }
    ((HierarchyProperty) localProperty).fireValueWillBeChanged(pOldValue, pNewValue, pOnRemoved, pAttributes);
  }

  protected void fireValueChanged(@Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Set<Object> pAttributes)
  {
    IProperty localProperty = getProperty();
    getHierarchy().fireValueChanged(localProperty, pOldValue, pNewValue, pAttributes);
    AbstractNode localParent = getParent();
    if (localParent != null)
    {
      for (IPropertyPitEventListener eventListener : localParent.listeners)
        //noinspection unchecked
        eventListener.propertyValueChanged(localProperty, pOldValue, pNewValue, pAttributes);
    }
    ((HierarchyProperty) localProperty).fireValueChanged(pOldValue, pNewValue, pAttributes);
  }

  protected void firePropertyAdded(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().firePropertyAdded(ppp, pPropertyDescription, pAttributes);
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked,ConstantConditions
      listener.propertyAdded(ppp, pPropertyDescription, pAttributes);
  }

  protected void removeProperty(@Nonnull IProperty pProperty, @Nonnull Runnable pApplyRemove, @Nonnull Set<Object> pAttributes)
  {
    if (!pProperty.isDynamic())
      throw new IllegalStateException("can't remove: " + pProperty);
    if (!(getValue() instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + pProperty);
    IPropertyDescription description = pProperty.getDescription();
    INode node = findNode(description.getName());
    if (node == null || !pProperty.equals(node.getProperty()))
      throw new IllegalStateException("can't remove: " + pProperty);
    ListenerList<Runnable> onFinish = new ListenerList<>();
    firePropertyWillBeRemoved(pProperty, onFinish::addStrongListener, pAttributes);
    pApplyRemove.run();
    firePropertyRemoved(description, pAttributes);
    onFinish.forEach(Runnable::run);
  }

  protected void firePropertyWillBeRemoved(@Nonnull IProperty pProperty, @Nonnull Consumer<Runnable> pOnRemoved,
                                           @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    getHierarchy().firePropertyWillBeRemoved(pProperty, pOnRemoved, pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyWillBeRemoved(pProperty, pOnRemoved, pAttributes);
    ((HierarchyProperty) pProperty).fireWillBeRemoved(pOnRemoved, pAttributes);
  }

  protected void firePropertyRemoved(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().firePropertyRemoved(ppp, pPropertyDescription, pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyRemoved(ppp, pPropertyDescription, pAttributes);
  }

  protected void changeOrder(@Nonnull Runnable pApplyOrderChange, @Nonnull Set<Object> pAttributes)
  {
    ListenerList<Runnable> onFinish = new ListenerList<>();
    firePropertyOrderWillBeChanged(onFinish::addStrongListener, pAttributes);
    pApplyOrderChange.run();
    firePropertyOrderChanged(pAttributes);
    onFinish.forEach(Runnable::run);
  }

  protected void firePropertyOrderWillBeChanged(@Nonnull Consumer<Runnable> pOnRemoved, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().fireChildrenOrderWillBeChanged(ppp, pOnRemoved, pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyOrderWillBeChanged(ppp, pOnRemoved, pAttributes);
  }

  protected void firePropertyOrderChanged(@Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().fireChildrenOrderChanged(ppp, pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyOrderChanged(ppp, pAttributes);
  }

  protected void firePropertyNameChanged(@Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    IProperty localProperty = getProperty();
    getHierarchy().firePropertyRenamed(localProperty, pOldName, pNewName, pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyNameChanged(localProperty, pOldName, pNewName, pAttributes);
    ((HierarchyProperty) localProperty).fireNameChanged(pOldName, pNewName, pAttributes);
  }

  @Override
  public String toString()
  {
    if (isValid())
      return PropertlyUtility.asString(this, "path=" + new PropertyPath(getProperty()), "value=" + getProperty().getValue());
    return PropertlyUtility.asString(this, "invalid");
  }

  protected void ensureValid()
  {
    if (!isValid())
      throw new NullPointerException("node is invalid.");
  }

}
