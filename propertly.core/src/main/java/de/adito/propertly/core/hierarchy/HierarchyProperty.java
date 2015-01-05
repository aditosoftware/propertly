package de.adito.propertly.core.hierarchy;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.PropertlyUtility;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author PaL
 *         Date: 30.01.13
 *         Time: 00:30
 */
class HierarchyProperty implements IProperty
{

  private AbstractNode node;
  private IPropertyDescription propertyDescription;
  private List<IPropertyEventListener> listeners;

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
    return node.getValue();
  }

  @Override
  public Object setValue(Object pValue)
  {
    return node.setValue(pValue);
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
  public IPropertyPitProvider getParent()
  {
    INode parent = node.getParent();
    return parent == null ? null : (IPropertyPitProvider) parent.getValue();
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
  public void addPropertyEventListener(@Nonnull IPropertyEventListener pListener)
  {
    if (listeners == null)
      listeners = new ArrayList<IPropertyEventListener>();
    listeners.add(pListener);
  }

  @Override
  public void removePropertyEventListener(@Nonnull IPropertyEventListener pListener)
  {
    if (listeners != null)
      listeners.remove(pListener);
  }

  void fire(Object pOldValue, Object pNewValue)
  {
    if (listeners != null)
      for (IPropertyEventListener listener : listeners)
        //noinspection unchecked
        listener.propertyChange(this, pOldValue, pNewValue);
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
