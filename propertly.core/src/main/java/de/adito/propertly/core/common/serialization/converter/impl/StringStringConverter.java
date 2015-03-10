package de.adito.propertly.core.common.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 09.03.15
 */
public class StringStringConverter extends AbstractObjectStringConverter<String>
{
  public StringStringConverter()
  {
    super(String.class);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull String pValue)
  {
    return pValue;
  }

  @Nullable
  @Override
  protected String stringToValue(@Nonnull String pValueAsString)
  {
    return pValueAsString;
  }
}
