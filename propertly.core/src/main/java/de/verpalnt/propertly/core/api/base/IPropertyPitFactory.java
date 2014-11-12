package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.IPropertyPit;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

/**
 * @author PaL, 09.11.13
 */
public interface IPropertyPitFactory
{

  <S extends IPropertyPitProvider> IPropertyPit<S, Object> create(S pPropertyPitProvider);

}
