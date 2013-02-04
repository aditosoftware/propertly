package de.verpalnt.propertly.core.listener;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;

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
  public void propertyAdded(IPropertyDescription<S, T> pPropertyDescription)
  {
  }

  @Override
  public void propertyRemoved(IPropertyDescription<S, T> pPropertyDescription)
  {
  }
}
