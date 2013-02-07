package de.verpalnt.propertly.core.common;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

/**
 * @author PaL
 *         Date: 25.11.12
 *         Time: 14:38
 */
public abstract class PropertyEventAdapter<S extends IPropertyPitProvider, T> implements IPropertyEventListener<S, T>
{
  @Override
  public void propertyChange(IProperty<S, T> pProperty, T pOldValue, T pNewValue)
  {
  }

  @Override
  public void propertyAdded(IPropertyPitProvider<S> pSource, IPropertyDescription<S, T> pPropertyDescription)
  {
  }

  @Override
  public void propertyRemoved(IPropertyPitProvider<S> pSource, IPropertyDescription<S, T> pPropertyDescription)
  {
  }
}
