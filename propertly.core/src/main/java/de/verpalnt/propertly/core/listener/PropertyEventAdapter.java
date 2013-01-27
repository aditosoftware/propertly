package de.verpalnt.propertly.core.listener;

import de.verpalnt.propertly.core.IProperty;

/**
 * @author PaL
 *         Date: 25.11.12
 *         Time: 14:38
 */
public abstract class PropertyEventAdapter<S, T> implements IPropertyEventListener<S, T>
{
  @Override
  public void propertyChange(IProperty<S, T> pProperty, T pOldValue, T pNewValue)
  {
  }

  @Override
  public void propertyAdded(IProperty<S, T> pProperty)
  {
  }

  @Override
  public void propertyRemoved(IProperty<S, T> pProperty)
  {
  }
}
