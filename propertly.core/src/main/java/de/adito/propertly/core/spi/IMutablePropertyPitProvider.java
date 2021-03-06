package de.adito.propertly.core.spi;

import org.jetbrains.annotations.NotNull;

/**
 * An IPropertyPitProvider which return an IMutablePropertyPit instead of a IPropertyPit.
 *
 * @author PaL
 *         Date: 31.01.13
 *         Time: 22:55
 */
public interface IMutablePropertyPitProvider<P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
    extends IPropertyPitProvider<P, S, T>
{

  /**
   * @return this IMutablePropertyPitProvider's IMutablePropertyPit.
   */
  @NotNull
  @Override
  IMutablePropertyPit<P, S, T> getPit();

}
