package de.adito.propertly.core.spi;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;

/**
 * IPropertyDescription gives static detail about a IProperty.
 *
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:39
 */
public interface IPropertyDescription<S extends IPropertyPitProvider, T> extends AnnotatedElement
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

  /**
   * @param pNewName if not null the name of the created IPropertyDescription.
   * @return a copy of the current IPropertyDescription optionally with a new name.
   */
  IPropertyDescription<S, T> copy(@Nullable String pNewName);

}
