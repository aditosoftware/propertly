package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class ShortStringConverter extends AbstractObjectStringConverter<Short>
{
  public ShortStringConverter()
  {
    super(Short.class);
  }

  @Nullable
  @Override
  protected Short stringToValue(@Nonnull String pValueAsString)
  {
    return Short.parseShort(pValueAsString);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Short pValue)
  {
    return pValue.toString();
  }
}
