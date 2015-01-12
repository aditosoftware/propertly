package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitEventListener;
import de.adito.propertly.core.spi.IPropertyPitProvider;

/**
 * An adapter implementation for IPropertyPitEventListener implementations.
 *
 * @author PaL
 *         Date: 25.11.12
 *         Time: 14:38
 */
public abstract class PropertyPitEventAdapter<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    implements IPropertyPitEventListener<P, S, T>
{
  @Override
  public void propertyChanged(IProperty<S, T> pProperty, T pOldValue, T pNewValue)
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
