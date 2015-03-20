package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;
import java.awt.*;
import java.util.StringTokenizer;

/**
 * @author j.boesl, 05.03.15
 */
public class DimensionStringConverter extends AbstractObjectStringConverter<Dimension>
{
  public DimensionStringConverter()
  {
    super(Dimension.class);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Dimension pValue)
  {
    return pValue.width + ", " + pValue.height;
  }

  @Nullable
  @Override
  protected Dimension stringToValue(@Nonnull String pValueAsString)
  {
    StringTokenizer stringTokenizer = new StringTokenizer(pValueAsString, ", ");
    if (stringTokenizer.countTokens() != 2)
      return null;
    return new Dimension(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
  }
}
