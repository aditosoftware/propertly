package de.adito.propertly.core.common.exception;

/**
 * An exception indicating that a field has the wrong modifiers.
 *
 * @author PaL
 *         Date: 09.01.15
 *         Time. 22:46
 */
public class WrongModifiersException extends RuntimeException
{

  public WrongModifiersException(String message)
  {
    super(message);
  }

}
