package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Dimension pSource)
      {
        return pSource.width + ", " + pSource.height;
      }

      @Nullable
      @Override
      public Dimension targetToSource(@NotNull String pTarget)
      {
        StringTokenizer stringTokenizer = new StringTokenizer(pTarget, ", ");
        if (stringTokenizer.countTokens() != 2)
          return null;
        return new Dimension(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
      }
    });
  }
}
