package de.adito.propertly.core.api.base;

import de.adito.propertly.core.api.*;

/**
 * Created by PaL on 09.11.13.
 */
public interface IMutablePropertyPitFactory
{

  <P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
  IMutablePropertyPit<P, S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType);

}
