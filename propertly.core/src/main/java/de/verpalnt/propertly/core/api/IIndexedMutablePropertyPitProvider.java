package de.verpalnt.propertly.core.api;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:05
 */
public interface IIndexedMutablePropertyPitProvider<S extends IIndexedMutablePropertyPitProvider, T> extends IMutablePropertyPitProvider<S, T>
{

  @Override
  IIndexedMutablePropertyPit<S, T> getPit();

}
