package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

/**
 * Created by PaL on 09.11.13.
 */
public interface IMutablePropertyPitFactory
{

  <S extends IMutablePropertyPitProvider, T> IMutablePropertyPit<S, T> create(S pPropertyPitProvider,
                                                                              Class<T> pAllowedChildType);

}
