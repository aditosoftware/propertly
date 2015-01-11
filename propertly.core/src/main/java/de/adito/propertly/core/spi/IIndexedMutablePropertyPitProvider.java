package de.adito.propertly.core.spi;

import javax.annotation.Nonnull;

/**
 * An IPropertyPitProvider which return an IIndexedMutablePropertyPit instead of a IPropertyPit.
 *
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:05
 */
public interface IIndexedMutablePropertyPitProvider<P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
    extends IMutablePropertyPitProvider<P, S, T>
{

  /**
   * @return this IIndexedMutablePropertyPitProvider's IIndexedMutablePropertyPit.
   */
  @Nonnull
  @Override
  IIndexedMutablePropertyPit<P, S, T> getPit();

}
