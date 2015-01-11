package de.adito.propertly.core.spi;

/**
 * IPropertyPitFactory creates an IPropertyPit which can be used for IPropertyPitProviders.
 *
 * @author PaL, 09.11.13
 */
public interface IPropertyPitFactory
{

  /**
   * @param pPropertyPitProvider the IPropertyPitProvider this IPropertyPit is created for.
   * @param <P>                  parent - the parent type.
   * @param <S>                  self - the own type.
   * @param <T>                  target - the child type.
   * @return an IPropertyPit for usage at an IPropertyPitProvider.
   */
  <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
  IPropertyPit<P, S, T> create(S pPropertyPitProvider);

}
