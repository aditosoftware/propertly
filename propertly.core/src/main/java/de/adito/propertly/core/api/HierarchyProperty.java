package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyEventListener;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.exception.InaccessibleException;
import de.adito.propertly.core.common.exception.PropertlyRenameException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
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
    if (canRead())
      return node.getValue();
    throw new InaccessibleException("IProperty '" + getDescription() + "' can't be read.");
  }

  @Override
  public Object setValue(Object pValue)
  {
    if (!canWrite())
      throw new InaccessibleException("IProperty '" + getDescription() + "' can't be written.");
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
