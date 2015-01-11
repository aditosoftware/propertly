package de.adito.propertly.core.spi;

/**
 * IMutablePropertyPitFactory creates an IMutablePropertyPit which can be used for IMutablePropertyPitProviders.
 *
 * @author PaL
 *         Date: 09.11.13.
 */
public interface IMutablePropertyPitFactory
{

  /**
   * @param pPropertyPitProvider the IMutablePropertyPitProvider this IMutablePropertyPit is created for.
   * @param <P>                  parent - the parent type.
   * @param <S>                  self - the own type.
   * @param <T>                  target - the child type.
   * @return an IMutablePropertyPit for usage at an IMutablePropertyPitProvider.
   */
  <P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
  IMutablePropertyPit<P, S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType);

}
