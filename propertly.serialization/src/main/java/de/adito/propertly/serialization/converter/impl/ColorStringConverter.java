package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;
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
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Color pSource)
      {
        return Integer.toString(pSource.getRGB());
      }

      @Nullable
      @Override
      public Color targetToSource(@Nonnull String pTarget)
      {
        return new Color(Integer.parseInt(pTarget));
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Color, Number>(Number.class)
    {
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Color pSource)
      {
        return pSource.getRGB();
      }

      @Nullable
      @Override
      public Color targetToSource(@Nonnull Number pTarget)
      {
        return new Color(pTarget.intValue());
      }
    });
  }
}
