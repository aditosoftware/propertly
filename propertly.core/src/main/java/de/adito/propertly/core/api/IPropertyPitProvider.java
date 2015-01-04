package de.adito.propertly.core.api;

import javax.annotation.Nonnull;

/**
 * IPropertyPitProvider gives access to an IPropertyPit and is used to define all IProperty objects for its IPropertyPit.<br/>
 * Since IPropertyPitProvider shall be used to implement descriptions and access methods the interface is as small as
 * possible to prevent signature conflicts with other implementations.
 *
 * @author PaL
 *         Date: 14.10.12
 *         Time: 14:52
 */
public interface IPropertyPitProvider<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
{

  /**
   * @return this IPropertyPitProvider's IPropertyPit.
   */
  @Nonnull
  IPropertyPit<P, S, T> getPit();

}
