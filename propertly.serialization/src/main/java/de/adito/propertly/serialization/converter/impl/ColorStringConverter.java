package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;
import java.awt.*;

/**
 * @author j.boesl, 05.03.15
 */
public class ColorStringConverter extends AbstractObjectStringConverter<Color>
{
  public ColorStringConverter()
  {
    super(Color.class);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Color pValue)
  {
    return Integer.toString(pValue.getRGB());
  }

  @Nullable
  @Override
  protected Color stringToValue(@Nonnull String pValueAsString)
  {
    return new Color(Integer.parseInt(pValueAsString));
  }
}
