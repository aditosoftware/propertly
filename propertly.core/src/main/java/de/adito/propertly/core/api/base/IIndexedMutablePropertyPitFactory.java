package de.adito.propertly.core.api.base;

import de.adito.propertly.core.api.*;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:28
 */
public interface IIndexedMutablePropertyPitFactory
{

  <P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
  IIndexedMutablePropertyPit<P, S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType);

}
