package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class ByteStringConverter extends AbstractObjectStringConverter<Byte>
{
  public ByteStringConverter()
  {
    super(Byte.class);
  }

  @Nullable
  @Override
  protected Byte stringToValue(@Nonnull String pValueAsString)
  {
    int value = Integer.decode(pValueAsString);
    if (value >= -128 && value <= 255)
    {
      return (byte) value;
    }
    else
    {
      throw new NumberFormatException("For input string: \"" + pValueAsString + '\"');
    }
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Byte pValue)
  {
    return pValue.toString();
  }
}
