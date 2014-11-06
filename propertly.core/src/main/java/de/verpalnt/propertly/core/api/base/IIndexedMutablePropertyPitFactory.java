package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:28
 */
public interface IIndexedMutablePropertyPitFactory
{

  <S extends IIndexedMutablePropertyPitProvider, T> IIndexedMutablePropertyPit<S, T> create(S pPropertyPitProvider,
                                                                                            Class<T> pAllowedChildType);

}
