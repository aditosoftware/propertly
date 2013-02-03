package de.verpalnt.propertly.core;

/**
 * @author PaL
 *         Date: 31.01.13
 *         Time: 22:55
 */
public interface IMutablePropertyPitProvider<S extends IMutablePropertyPitProvider<S, T>, T> extends IPropertyPitProvider<S>
{

  @Override
  MutablePropertyPit<S, T> getPit();

}