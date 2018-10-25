package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author j.boesl, 05.03.15
 */
public class ColorStringConverter extends AbstractObjectConverter<Color>
{
  public ColorStringConverter()
  {
    super(Color.class);
    registerSourceTargetConverter(new SourceTargetConverter<Color, String>(String.class)
    {
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Color pSource)
      {
        return Integer.toString(pSource.getRGB());
      }

      @Nullable
      @Override
      public Color targetToSource(@NotNull String pTarget)
      {
        return new Color(Integer.parseInt(pTarget));
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Color, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Color pSource)
      {
        return pSource.getRGB();
      }

      @Nullable
      @Override
      public Color targetToSource(@NotNull Number pTarget)
      {
        return new Color(pTarget.intValue());
      }
    });
  }
}
