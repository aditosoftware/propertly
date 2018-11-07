package de.adito.propertly.reactive.api;

/**
 * Exception for onError-Processing inside Propertly Observables
 *
 * @author w.glanzer, 01.11.2018
 */
@SuppressWarnings("unused")
public class PropertlyReactiveException extends Exception
{

  public PropertlyReactiveException()
  {
  }

  public PropertlyReactiveException(String message)
  {
    super(message);
  }

  public PropertlyReactiveException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public PropertlyReactiveException(Throwable cause)
  {
    super(cause);
  }

}
