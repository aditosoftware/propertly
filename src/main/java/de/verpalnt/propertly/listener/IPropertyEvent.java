package de.verpalnt.propertly.listener;

import de.verpalnt.propertly.IProperty;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyEvent<S, T>
{

  IProperty<S, T> getProperty();

  T oldValue();

}
