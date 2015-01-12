package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.IPropertyPitProvider;

/**
 * An factory interface that is used to build another object like this one. The created object is not a clone but a
 * fresh created one.
 *
 * @author j.boesl, 20.11.14
 */
public interface IReconstructor<T extends IPropertyPitProvider>
{

  /**
   * Recreates this object.
   *
   * @return a new instance of this object's class.
   */
  T create();

}
