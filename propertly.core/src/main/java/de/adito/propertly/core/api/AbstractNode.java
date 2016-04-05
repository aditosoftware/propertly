package de.adito.propertly.core.api;

import de.adito.propertly.core.common.*;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.Set;

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
    if (hierarchy == null)
      throw new NullPointerException("node is invalid");
    return hierarchy;
  }

  @Nullable
  @Override
  public AbstractNode getParent()
  {
    return parent;
  }

  @Nonnull
  @Override
  public IProperty getProperty()
  {
    if (property == null)
      throw new NullPointerException("node is invalid");
    return property;
  }

  @Override
  public boolean isValid()
  {
    return hierarchy != null;
  }

  @Override
  public void remove()
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

  protected void fireValueChange(@Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    getHierarchy().fireNodeChanged(getProperty(), pOldValue, pNewValue, pAttributes);
    AbstractNode p = getParent();
    if (p != null)
    {
      for (IPropertyPitEventListener eventListener : p.listeners)
        //noinspection unchecked
        eventListener.propertyChanged(getProperty(), pOldValue, pNewValue, pAttributes);
    }
    property.fire(pOldValue, pNewValue, pAttributes);
  }

  protected void fireNodeAdded(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    getHierarchy().firePropertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription, pAttributes);
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked,ConstantConditions
      listener.propertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription, pAttributes);
  }

  protected void fireNodeWillBeRemoved(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    getHierarchy().firePropertyWillBeRemoved((IPropertyPitProvider) getValue(), pPropertyDescription, pAttributes);
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked,ConstantConditions
      listener.propertyWillBeRemoved((IPropertyPitProvider) getValue(), pPropertyDescription, pAttributes);
  }

  protected void fireNodeRemoved(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    getHierarchy().firePropertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription, pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription, pAttributes);
  }

  protected void firePropertyOrderChanged(@Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    getHierarchy().fireChildrenOrderChanged((IPropertyPitProvider) getValue(), pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyOrderChanged((IPropertyPitProvider) getValue(), pAttributes);
  }

  protected void firePropertyNameChanged(@Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    getHierarchy().fireNodeRenamed(getProperty(), pOldName, pNewName, pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyNameChanged(getProperty(), pOldName, pNewName, pAttributes);
    property.firePropertyNameChanged(pOldName, pNewName, pAttributes);
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
