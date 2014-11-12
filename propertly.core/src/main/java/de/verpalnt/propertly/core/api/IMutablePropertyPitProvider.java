package de.verpalnt.propertly.core.api;

/**
 * @author PaL
 *         Date: 31.01.13
 *         Time: 22:55
 */
public interface IMutablePropertyPitProvider<S extends IMutablePropertyPitProvider, T> extends IPropertyPitProvider<S, T>
{

  @Override
  IMutablePropertyPit<S, T> getPit();

}
