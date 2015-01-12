package de.adito.propertly.core.common;

/**
 * A simple function interface.
 *
 * @author PaL
 *         Date: 13.07.14
 *         Time. 16:39
 */
public interface IFunction<In, Out>
{

  /**
   * Executes this function.
   *
   * @param pIn the given parameter.
   * @return the result.
   */
  Out run(In pIn);

}
