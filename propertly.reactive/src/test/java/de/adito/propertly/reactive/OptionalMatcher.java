package de.adito.propertly.reactive;

import org.mockito.Mockito;

import java.util.*;

/**
 * @author w.glanzer, 09.11.2018
 */
public class OptionalMatcher
{

  public static <T> T optionalEmpty()
  {
    return Mockito.argThat(argument -> argument instanceof Optional && !((Optional) argument).isPresent());
  }

  public static <T> T optionalIsPresent()
  {
    return optionalWithValue(Object.class);
  }

  public static <T> T optionalWithValue(Class<?> pValueClass)
  {
    return Mockito.argThat(argument -> argument instanceof Optional && ((Optional) argument).isPresent() && pValueClass.isAssignableFrom(((Optional) argument).get().getClass()));
  }

  public static <T> T optionalWithValue(T pValue)
  {
    return Mockito.argThat(argument -> argument instanceof Optional && ((Optional) argument).isPresent() && Objects.equals(((Optional) argument).get(), pValue));
  }

}
