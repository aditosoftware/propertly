package de.verpalnt.propertly.core;

/**
 * @author PaL
 *         Date: 31.01.13
 *         Time: 22:55
 */
public interface IMutablePropertyPitProvider<S extends IMutablePropertyPitProvider, T> extends IPropertyPitProvider<S>
{

  @Override
  IMutablePropertyPit<S, T> getPit();

  T getChildType();

}
