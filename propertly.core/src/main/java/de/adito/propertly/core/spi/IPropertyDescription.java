package de.adito.propertly.core.spi;

import java.lang.reflect.AnnotatedElement;

/**
 * IPropertyDescription gives static detail about a IProperty.
 *
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:39
 */
public interface IPropertyDescription<S extends IPropertyPitProvider, T> extends AnnotatedElement, Comparable<IPropertyDescription<?, ?>>
{

  /**
   * @return the IPropertyPitProvider's class where this IPropertyDescription is defined.
   */
  Class<S> getSourceType();

  /**
   * @return the class for the IProperty's value.
   */
  Class<? extends T> getType();

  /**
   * @return the name for the corresponding IProperty.
   */
  String getName();

}
