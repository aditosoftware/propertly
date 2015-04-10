package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;
import java.awt.*;

/**
 * @author j.boesl, 09.03.15
 */
public class FontStringConverter extends AbstractObjectConverter<Font>
{
  public FontStringConverter()
  {
    super(Font.class);
    registerSourceTargetConverter(new SourceTargetConverter<Font, String>(String.class)
    {
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Font pSource)
      {
        return pSource.getName();
      }

      @Nullable
      @Override
      public Font targetToSource(@Nonnull String pTarget)
      {
        return Font.decode(pTarget);
      }
    });
  }
}
