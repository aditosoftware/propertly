package de.adito.propertly.core.api;

/**
 * @author PaL
 *         Date: 31.01.13
 *         Time: 22:55
 */
public interface IMutablePropertyPitProvider<P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
    extends IPropertyPitProvider<P, S, T>
{

  @Override
  IMutablePropertyPit<P, S, T> getPit();

}
