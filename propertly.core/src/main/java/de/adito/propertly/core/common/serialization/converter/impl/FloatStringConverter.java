package de.adito.propertly.core.common.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class FloatStringConverter extends AbstractObjectStringConverter<Float>
{
  public FloatStringConverter()
  {
    super(Float.class);
  }

  @Nullable
  @Override
  protected Float stringToValue(@Nonnull String pValueAsString)
  {
    return Float.parseFloat(pValueAsString);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Float pValue)
  {
    return pValue.toString();
  }
}
