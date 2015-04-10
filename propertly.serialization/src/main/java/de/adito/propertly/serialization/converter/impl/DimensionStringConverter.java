package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;
import java.awt.*;
import java.util.StringTokenizer;

/**
 * @author j.boesl, 05.03.15
 */
public class DimensionStringConverter extends AbstractObjectConverter<Dimension>
{
  public DimensionStringConverter()
  {
    super(Dimension.class);
    registerSourceTargetConverter(new SourceTargetConverter<Dimension, String>(String.class)
    {
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Dimension pSource)
      {
        return pSource.width + ", " + pSource.height;
      }

      @Nullable
      @Override
      public Dimension targetToSource(@Nonnull String pTarget)
      {
        StringTokenizer stringTokenizer = new StringTokenizer(pTarget, ", ");
        if (stringTokenizer.countTokens() != 2)
          return null;
        return new Dimension(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
      }
    });
  }
}
