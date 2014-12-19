package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

/**
 * @author PaL, 09.11.13
 */
public interface IPropertyPitFactory
{

  <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
  IPropertyPit<P, S, T> create(S pPropertyPitProvider);

}
