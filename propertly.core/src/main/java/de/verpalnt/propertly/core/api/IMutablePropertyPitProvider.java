package de.verpalnt.propertly.core.api;

import de.verpalnt.propertly.core.hierarchy.MutablePropertyPit;

/**
 * @author PaL
 *         Date: 31.01.13
 *         Time: 22:55
 */
public interface IMutablePropertyPitProvider<S extends IMutablePropertyPitProvider, T> extends IPropertyPitProvider<S>
{

  @Override
  MutablePropertyPit<S, T> getPit();

}
