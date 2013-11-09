package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.IPropertyPit;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

/**
 * Created by PaL on 09.11.13.
 */
public interface IPropertyPitFactory
{

  <S extends IPropertyPitProvider> IPropertyPit<S> create(S pPropertyPitProvider);

}
