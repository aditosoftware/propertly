package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.exception.*;
import de.adito.propertly.core.spi.*;
import de.adito.util.weak.MixedReferences;

import javax.annotation.*;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author PaL
 *         Date: 30.01.13
 *         Time: 00:30
 */
class HierarchyProperty implements IProperty
{

  private AbstractNode node;
  private IPropertyDescription propertyDescription;
  private MixedReferences<IPropertyEventListener> listeners;

  HierarchyProperty(AbstractNode pNode, IPropertyDescription pPropertyDescription)
  {
    node = pNode;
    propertyDescription = pPropertyDescription;
  }

  @Nonnull
  @Override
  public IPropertyDescription getDescription()
  {
    return propertyDescription;
  }

  void setPropertyDescription(IPropertyDescription pPropertyDescription)
  {
    propertyDescription = pPropertyDescription;
  }

  @Override
  public IHierarchy<?> getHierarchy()
  {
    IPropertyPitProvider parent = getParent();
    if (parent == null) {
      Object value = getValue();
      assert value instanceof IPropertyPitProvider;
      return ((IPropertyPitProvider) value).getPit().getHierarchy();
    }
    return parent.getPit().getHierarchy();
  }

  @Override
  public Object getValue()
  {
    if (canRead())
      return node.getValue();
    throw new InaccessibleException("IProperty '" + getDescription() + "' can't be read.");
  }

  @Override
  public Object setValue(Object pValue, @Nullable Object... pAttributes)
  {
    if (canWrite())
      return node.setValue(pValue, PropertlyUtility.toNonnullSet(pAttributes));
    throw new InaccessibleException("IProperty '" + getDescription() + "' can't be written.");
  }

  @Override
  public boolean canRead()
  {
    return node.canRead();
  }

  @Override
  public boolean canWrite()
  {
    return node.canWrite();
  }

  @Override
  public boolean isValid()
  {
    return node.isValid();
  }

  @Override
  public IPropertyPitProvider getParent()
  {
    INode parent = node.getParent();
    return parent == null ? null : (IPropertyPitProvider) parent.getValue();
  }

  @Nonnull
  @Override
  public Class getType()
  {
    return getDescription().getType();
  }

  @Nonnull
  @Override
  public String getName()
  {
    return getDescription().getName();
  }

  @Override
  public void rename(@Nonnull String pName, @Nullable Object... pAttributes) throws PropertlyRenameException
  {
    throw new PropertlyRenameException(this, pName, pAttributes);
  }

  @Override
  public boolean isDynamic()
  {
    return false;
  }

  @Override
  public synchronized void addWeakListener(@Nonnull IPropertyEventListener pListener)
  {
    if (listeners == null)
      listeners = new MixedReferences<>();
    listeners.addWeak(pListener);
  }

  @Override
  public synchronized void addStrongListener(@Nonnull IPropertyEventListener pListener)
  {
    if (listeners == null)
      listeners = new MixedReferences<>();
    listeners.addStrong(pListener);
  }

  @Override
  public synchronized void removeListener(@Nonnull IPropertyEventListener pListener)
  {
    if (listeners != null)
      listeners.remove(pListener);
  }

  void fireValueWillBeChanged(@Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Consumer<Runnable> pOnRemoved,
                              @Nonnull Set<Object> pAttributes)
  {
    synchronized (this) {
      if (listeners == null)
        return;
    }
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyValueWillBeChanged(this, pOldValue, pNewValue, pOnRemoved, pAttributes);
  }

  void fireValueChanged(@Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Set<Object> pAttributes)
  {
    synchronized (this) {
      if (listeners == null)
        return;
    }
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyValueChanged(this, pOldValue, pNewValue, pAttributes);
  }

  void fireNameChanged(@Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set<Object> pAttributes)
  {
    synchronized (this) {
      if (listeners == null)
        return;
    }
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyNameChanged(this, pOldName, pNewName, pAttributes);
  }

  void fireWillBeRemoved(@Nonnull Consumer<Runnable> pOnRemoved, @Nonnull Set<Object> pAttributes)
  {
    synchronized (this) {
      if (listeners == null)
        return;
    }
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyWillBeRemoved(this, pOnRemoved, pAttributes);
  }

  AbstractNode getNode()
  {
    return node;
  }

  @Override
  public String toString()
  {
    return PropertlyUtility.asString(this, getDescription().toString(), "value=" + getValue());
  }

}
