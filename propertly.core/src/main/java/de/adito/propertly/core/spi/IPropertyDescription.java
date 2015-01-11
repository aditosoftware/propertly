package de.adito.propertly.core.spi;

import de.adito.propertly.core.common.IAnnotationProvider;

/**
 * IPropertyDescription gives static detail about a IProperty.
 *
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:39
 */
public interface IPropertyDescription<S extends IPropertyPitProvider, T> extends IAnnotationProvider
{

  /**
   * @return the IPropertyPitProvider's class where this IPropertyDescription is defined.
   */
  Class<S> getSourceType();

  /**
   * @return the class for the IProperty's value.
   */
  Class<T> getType();

  /**
   * @return the name for the corresponding IProperty.
   */
  String getName();

}
