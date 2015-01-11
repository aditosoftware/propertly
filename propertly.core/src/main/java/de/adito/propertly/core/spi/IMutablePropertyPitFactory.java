package de.adito.propertly.core.spi;

/*
 * @author PaL
 *         Date: 09.11.13.
 */
public interface IMutablePropertyPitFactory
{

  <P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
  IMutablePropertyPit<P, S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType);

}
