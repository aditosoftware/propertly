package de.adito.propertly.core.common.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class IntegerStringConverter extends AbstractObjectStringConverter<Integer>
{
  public IntegerStringConverter()
  {
    super(Integer.class);
  }

  @Nullable
  @Override
  protected Integer stringToValue(@Nonnull String pValueAsString)
  {
    return Integer.parseInt(pValueAsString);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Integer pValue)
  {
    return pValue.toString();
  }
}
