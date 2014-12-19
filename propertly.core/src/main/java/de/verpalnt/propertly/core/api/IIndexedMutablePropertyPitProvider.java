package de.verpalnt.propertly.core.api;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:05
 */
public interface IIndexedMutablePropertyPitProvider<P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
    extends IMutablePropertyPitProvider<P, S, T>
{

  @Override
  IIndexedMutablePropertyPit<P, S, T> getPit();

}
