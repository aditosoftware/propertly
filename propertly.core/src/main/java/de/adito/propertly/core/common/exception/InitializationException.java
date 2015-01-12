package de.adito.propertly.core.common.exception;

/**
 * An exception that occurs when initialization fails.
 *
 * @author PaL
 *         Date: 09.01.15
 *         Time. 23:41
 */
public class InitializationException extends RuntimeException
{

  public InitializationException(String message)
  {
    super(message);
  }

}
