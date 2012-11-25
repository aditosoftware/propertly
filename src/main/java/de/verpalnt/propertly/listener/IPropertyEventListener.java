package de.verpalnt.propertly.listener;

import de.verpalnt.propertly.IProperty;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyEventListener<S, T>
{

  void propertyChange(IProperty<S, T> pProperty, T pOldValue, T pNewValue);

  void propertyAdded(IProperty<S, T> pProperty);

  void propertyRemoved(IProperty<S, T> pProperty);

}
