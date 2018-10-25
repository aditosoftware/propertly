package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitEventListener;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.util.weak.MixedReferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

  private _MixedReferences listeners;


  protected AbstractNode(@NotNull Hierarchy pHierarchy, @Nullable AbstractNode pParent,
                         @NotNull IPropertyDescription pPropertyDescription, boolean pDynamic)
  {
    hierarchy = pHierarchy;
    parent = pParent;
    if (pDynamic || parent == null)
      property = new DynamicHierarchyProperty(this, pPropertyDescription);
    else
      property = new HierarchyProperty(this, pPropertyDescription);
    listeners = new _MixedReferences();
  }

  @NotNull
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

  @NotNull
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
  public void addWeakListener(@NotNull IPropertyPitEventListener pListener)
  {
    ensureValid();
    listeners.addWeak(pListener);
  }

  @Override
  public void addStrongListener(@NotNull IPropertyPitEventListener pListener)
  {
    ensureValid();
    listeners.addStrong(pListener);
  }

  @Override
  public void removeListener(@NotNull IPropertyPitEventListener pListener)
  {
    ensureValid();
    listeners.remove(pListener);
  }

  protected void fireValueWillBeChange(@Nullable Object pOldValue, @Nullable Object pNewValue, @NotNull Consumer<Runnable> pOnRemoved,
                                       @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    HierarchyProperty localProperty = (HierarchyProperty) getProperty();
    getHierarchy().fireValueWillBeChanged(localProperty, pOldValue, pNewValue, pOnRemoved, pAttributes);
    AbstractNode localParent = getParent();
    if (localParent != null) {
      if (localParent.listeners != null) {
        List<IPropertyPitEventListener> parentalListeners = localParent.listeners.getObjects();
        for (IPropertyPitEventListener eventListener : parentalListeners)
          //noinspection unchecked
          eventListener.propertyValueWillBeChanged(localProperty, pOldValue, pNewValue, pOnRemoved, pAttributes);
      }
    }
    localProperty.fireValueWillBeChanged(pOldValue, pNewValue, pOnRemoved, pAttributes);
  }

  protected void fireValueChange(@Nullable Object pOldValue, @Nullable Object pNewValue, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    HierarchyProperty localProperty = (HierarchyProperty) getProperty();
    getHierarchy().fireValueChanged(localProperty, pOldValue, pNewValue, pAttributes);
    AbstractNode localParent = getParent();
    if (localParent != null) {
      if (localParent.listeners != null) {
        List<IPropertyPitEventListener> parentalListeners = localParent.listeners.getObjects();
        for (IPropertyPitEventListener eventListener : parentalListeners)
          //noinspection unchecked
          eventListener.propertyValueChanged(localProperty, pOldValue, pNewValue, pAttributes);
      }
    }
    localProperty.fireValueChanged(pOldValue, pNewValue, pAttributes);
  }

  protected void fireNodeAdded(@NotNull IPropertyDescription pPropertyDescription, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().firePropertyAdded(ppp, pPropertyDescription, pAttributes);
    if (listeners != null) {
      List<IPropertyPitEventListener> localListeners = listeners.getObjects();
      for (IPropertyPitEventListener listener : localListeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyAdded(ppp, pPropertyDescription, pAttributes);
    }
  }

  protected void fireNodeWillBeRemoved(@NotNull IPropertyDescription pPropertyDescription, @NotNull Consumer<Runnable> pOnRemoved,
                                       @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    HierarchyProperty property = (HierarchyProperty) ppp.getPit().getProperty(pPropertyDescription);
    getHierarchy().firePropertyWillBeRemoved(property, pOnRemoved, pAttributes);
    if (listeners != null) {
      List<IPropertyPitEventListener> localListeners = listeners.getObjects();
      for (IPropertyPitEventListener listener : localListeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyWillBeRemoved(property, pOnRemoved, pAttributes);
    }
    property.fireWillBeRemoved(pOnRemoved, pAttributes);
  }

  protected void fireNodeRemoved(@NotNull IPropertyDescription pPropertyDescription, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().firePropertyRemoved(ppp, pPropertyDescription, pAttributes);
    if (listeners != null) {
      List<IPropertyPitEventListener> localListeners = listeners.getObjects();
      for (IPropertyPitEventListener listener : localListeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyRemoved(ppp, pPropertyDescription, pAttributes);
    }
  }

  protected void firePropertyOrderWillBeChanged(@NotNull Consumer<Runnable> pOnRemoved, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().fireChildrenOrderWillBeChanged(ppp, pOnRemoved, pAttributes);
    if (listeners != null) {
      List<IPropertyPitEventListener> localListeners = listeners.getObjects();
      for (IPropertyPitEventListener listener : localListeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyOrderWillBeChanged(ppp, pOnRemoved, pAttributes);
    }
  }

  protected void firePropertyOrderChanged(@NotNull Set<Object> pAttributes)
  {
    ensureValid();
    IPropertyPitProvider ppp = (IPropertyPitProvider) getValue();
    getHierarchy().fireChildrenOrderChanged(ppp, pAttributes);
    if (listeners != null) {
      List<IPropertyPitEventListener> localListeners = listeners.getObjects();
      for (IPropertyPitEventListener listener : localListeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyOrderChanged(ppp, pAttributes);
    }
  }

  protected void firePropertyNameChanged(@NotNull String pOldName, @NotNull String pNewName, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    HierarchyProperty localProperty = (HierarchyProperty) getProperty();
    getHierarchy().firePropertyRenamed(localProperty, pOldName, pNewName, pAttributes);
    if (listeners != null) {
      List<IPropertyPitEventListener> localListeners = listeners.getObjects();
      for (IPropertyPitEventListener listener : localListeners)
        //noinspection unchecked,ConstantConditions
        listener.propertyNameChanged(localProperty, pOldName, pNewName, pAttributes);
    }
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

  /**
   * {@link MixedReferences}-implementation with direct access to a copy of the stored objects.
   */
  private class _MixedReferences extends MixedReferences<IPropertyPitEventListener>
  {
    @NotNull
    @Override
    public List<IPropertyPitEventListener> getObjects()
    {
      return super.getObjects();
    }
  }

}
