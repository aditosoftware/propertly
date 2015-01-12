package de.adito.propertly.core.common.exception;

/**
 * An exception indicating that accessing something wasn't possible.
 *
 * @author PaL
 *         Date: 07.01.15
 *         Time. 23:24
 */
public class InaccessibleException extends RuntimeException
{

  public InaccessibleException(String message)
  {
    super(message);
  }

}
