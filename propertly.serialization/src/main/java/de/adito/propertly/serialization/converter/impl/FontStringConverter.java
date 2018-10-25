package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Font pSource)
      {
        return pSource.getName();
      }

      @Nullable
      @Override
      public Font targetToSource(@NotNull String pTarget)
      {
        return Font.decode(pTarget);
      }
    });
  }
}
