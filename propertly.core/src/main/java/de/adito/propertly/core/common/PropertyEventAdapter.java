package de.adito.propertly.core.common;

import de.adito.propertly.core.api.*;

/**
 * @author PaL
 *         Date: 25.11.12
 *         Time: 14:38
 */
public abstract class PropertyEventAdapter<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    implements IPropertyEventListener<P, S, T>
{
  @Override
  public void propertyChange(IProperty<S, T> pProperty, T pOldValue, T pNewValue)
  {
  }

  @Override
  public void propertyAdded(IPropertyPitProvider<P, S, T> pSource, IPropertyDescription<S, T> pPropertyDescription)
  {
  }

  @Override
  public void propertyWillBeRemoved(IPropertyPitProvider<P, S, T> pSource, IPropertyDescription<S, T> pPropertyDescription)
  {
  }

  @Override
  public void propertyRemoved(IPropertyPitProvider<P, S, T> pSource, IPropertyDescription<S, T> pPropertyDescription)
  {
  }
}
