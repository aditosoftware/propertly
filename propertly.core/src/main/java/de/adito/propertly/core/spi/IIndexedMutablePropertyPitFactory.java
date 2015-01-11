package de.adito.propertly.core.spi;

/**
 * IIndexedMutablePropertyPitFactory creates an IIndexedMutablePropertyPit which can be used for IIndexedMutablePropertyPitProviders.
 *
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:28
 */
public interface IIndexedMutablePropertyPitFactory
{

  /**
   * @param pPropertyPitProvider the IIndexedMutablePropertyPitProvider this IIndexedMutablePropertyPit is created for.
   * @param <P>                  parent - the parent type.
   * @param <S>                  self - the own type.
   * @param <T>                  target - the child type.
   * @return an IIndexedMutablePropertyPit for usage at an IIndexedMutablePropertyPitProvider.
   */
  <P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
  IIndexedMutablePropertyPit<P, S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType);

}
