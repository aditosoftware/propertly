package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.*;
import de.adito.util.weak.MixedReferences;

import javax.annotation.*;
import java.util.Set;
import java.util.function.Consumer;

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

  private MixedReferences<IPropertyPitEventListener> listeners;


  protected AbstractNode(@Nonnull Hierarchy pHierarchy, @Nullable AbstractNode pParent,
                         @Nonnull IPropertyDescription pPropertyDescription, boolean pDynamic)
  {
    hierarchy = pHierarchy;
    parent = pParent;
    if (pDynamic || parent == null)
      property = new DynamicHierarchyProperty(this, pPropertyDescription);
    else
      property = new HierarchyProperty(this, pPropertyDescription);
    listeners = new MixedReferences<>();
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
    listeners.addWeak(pListener);
  }

  @Override
  public void addStrongListener(@Nonnull IPropertyPitEventListener pListener)
  {
    ensureValid();
    listeners.addStrong(pListener);
  }

  @Override
  public void removeListener(@Nonnull IPropertyPitEventListener pListener)
  {
    ensureValid();
    listeners.remove(pListener);
  }

  protected void fireValueWillBeChange(@Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Consumer<Runnable> pOnRemoved,
                                       @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    HierarchyProperty localProperty = (HierarchyProperty) getProperty();
    getHierarchy().fireValueWillBeChanged(localProperty, pOldValue, pNewValue, pOnRemoved, pAttributes);
    AbstractNode localParent = getParent();
    if (localParent != null) {
      for (IPropertyPitEventListener eventListener : localParent.listeners)
        //noinspection unchecked
        eventListener.propertyValueWillBeChanged(localProperty, pOldValue, pNewValue, pOnRemoved, pAttributes);
    }
    localProperty.fireValueWillBeChanged(pOldValue, pNewValue, pOnRemoved, pAttributes);
  }

  protected void fireValueChange(@Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    HierarchyProperty localProperty = (HierarchyProperty) getProperty();
    getHierarchy().fireValueChanged(localProperty, pOldValue, pNewValue, pAttributes);
    AbstractNode localParent = getParent();
    if (localParent != null) {
      for (IPropertyPitEventListener eventListener : localParent.listeners)
        //noinspection unchecked
        eventListener.propertyValueChanged(localProperty, pOldValue, pNewValue, pAttributes);
    }
    localProperty.fireValueChanged(pOldValue, pNewValue, pAttributes);
  }

  protected void fireNodeAdded(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().firePropertyAdded(ppp, pPropertyDescription, pAttributes);
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked,ConstantConditions
      listener.propertyAdded(ppp, pPropertyDescription, pAttributes);
  }

  protected void fireNodeWillBeRemoved(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Consumer<Runnable> pOnRemoved,
                                       @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    HierarchyProperty property = (HierarchyProperty) ppp.getPit().getProperty(pPropertyDescription);
    getHierarchy().firePropertyWillBeRemoved(property, pOnRemoved, pAttributes);
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked,ConstantConditions
      listener.propertyWillBeRemoved(property, pOnRemoved, pAttributes);
    property.fireWillBeRemoved(pOnRemoved, pAttributes);
  }

  protected void fireNodeRemoved(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().firePropertyRemoved(ppp, pPropertyDescription, pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyRemoved(ppp, pPropertyDescription, pAttributes);
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
    HierarchyProperty localProperty = (HierarchyProperty) getProperty();
    getHierarchy().firePropertyRenamed(localProperty, pOldName, pNewName, pAttributes);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyNameChanged(localProperty, pOldName, pNewName, pAttributes);
    localProperty.fireNameChanged(pOldName, pNewName, pAttributes);
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
