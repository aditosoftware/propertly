package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;
import java.awt.*;

/**
 * @author j.boesl, 09.03.15
 */
public class FontStringConverter extends AbstractObjectStringConverter<Font>
{
  public FontStringConverter()
  {
    super(Font.class);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Font pValue)
  {
    return pValue.getName();
  }

  @Nullable
  @Override
  protected Font stringToValue(@Nonnull String pValueAsString)
  {
    return Font.decode(pValueAsString);
  }
}
