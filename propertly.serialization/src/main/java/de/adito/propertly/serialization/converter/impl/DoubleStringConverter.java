package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class DoubleStringConverter extends AbstractObjectStringConverter<Double>
{
  public DoubleStringConverter()
  {
    super(Double.class);
  }

  @Nullable
  @Override
  protected Double stringToValue(@Nonnull String pValueAsString)
  {
    return Double.parseDouble(pValueAsString);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Double pValue)
  {
    return pValue.toString();
  }
}
