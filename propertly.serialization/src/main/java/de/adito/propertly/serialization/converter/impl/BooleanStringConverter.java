package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class BooleanStringConverter extends AbstractObjectStringConverter<Boolean>
{
  public BooleanStringConverter()
  {
    super(Boolean.class);
  }

  @Nullable
  @Override
  protected Boolean stringToValue(@Nonnull String pValueAsString)
  {
    return "true".equalsIgnoreCase(pValueAsString);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Boolean pValue)
  {
    return pValue.toString();
  }
}
