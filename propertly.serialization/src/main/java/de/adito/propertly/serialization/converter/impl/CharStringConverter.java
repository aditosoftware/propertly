package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class CharStringConverter extends AbstractObjectStringConverter<Character>
{
  public CharStringConverter()
  {
    super(Character.class);
  }

  @Nullable
  @Override
  protected Character stringToValue(@Nonnull String pValueAsString)
  {
    return pValueAsString.length() == 0 ? '\0' : pValueAsString.charAt(0);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Character pValue)
  {
    return pValue == '\0' ? "" : pValue.toString();
  }
}
