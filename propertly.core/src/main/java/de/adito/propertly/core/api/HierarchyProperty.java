package de.adito.propertly.core.api;

import de.adito.propertly.core.common.*;
import de.adito.propertly.core.common.exception.*;
import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author PaL
 *         Date: 30.01.13
 *         Time: 00:30
 */
class HierarchyProperty implements IProperty
{

  private AbstractNode node;
  private IPropertyDescription propertyDescription;
  private ListenerList<IPropertyEventListener> listeners;

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

  @Override
  public Object getValue()
  {
    if (canRead())
      return node.getValue();
    throw new InaccessibleException("IProperty '" + getDescription() + "' can't be read.");
  }

  @Override
  public Object setValue(Object pValue)
  {
    if (canWrite())
      return node.setValue(pValue);
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

  @Override
  public IPropertyPath getPath()
  {
    return node.getPath();
  }

  @Nonnull
  @Override
  public Class getType()
  {
    return propertyDescription.getType();
  }

  @Nonnull
  @Override
  public String getName()
  {
    return propertyDescription.getName();
  }

  @Override
  public void rename(@Nonnull String pName) throws PropertlyRenameException
  {
    throw new PropertlyRenameException(this, pName);
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
      listeners = new ListenerList<IPropertyEventListener>();
    listeners.addWeakListener(pListener);
  }

  @Override
  public synchronized void addStrongListener(@Nonnull IPropertyEventListener pListener)
  {
    if (listeners == null)
      listeners = new ListenerList<IPropertyEventListener>();
    listeners.addStrongListener(pListener);
  }

  @Override
  public synchronized void removeListener(@Nonnull IPropertyEventListener pListener)
  {
    if (listeners != null)
      listeners.removeListener(pListener);
  }

  void fire(Object pOldValue, Object pNewValue)
  {
    List<IPropertyEventListener> l;
    synchronized (this)
    {
      if (listeners == null)
        return;
      l = listeners.getListeners();
    }
    for (IPropertyEventListener listener : l)
      //noinspection unchecked
      listener.propertyChanged(this, pOldValue, pNewValue);
  }

  AbstractNode getNode()
  {
    return node;
  }

  @Override
  public String toString()
  {
    return PropertlyUtility.asString(this, propertyDescription.toString(), "value=" + getValue());
  }

}
