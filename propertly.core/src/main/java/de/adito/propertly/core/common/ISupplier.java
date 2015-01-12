package de.adito.propertly.core.common;

/**
 * An interface for something that supplies a value.
 *
 * @author PaL
 *         Date: 09.02.13
 *         Time: 19:17
 */
public interface ISupplier<T>
{

  /**
   * Gives the value this object supplies. The returned value can vary.
   *
   * @return the supplied value.
   */
  T get();

}
