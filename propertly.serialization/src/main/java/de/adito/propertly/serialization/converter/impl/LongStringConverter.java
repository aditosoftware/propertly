package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class LongStringConverter extends AbstractObjectStringConverter<Long>
{
  public LongStringConverter()
  {
    super(Long.class);
  }

  @Nullable
  @Override
  protected Long stringToValue(@Nonnull String pValueAsString)
  {
    return Long.parseLong(pValueAsString);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Long pValue)
  {
    return pValue.toString();
  }
}
