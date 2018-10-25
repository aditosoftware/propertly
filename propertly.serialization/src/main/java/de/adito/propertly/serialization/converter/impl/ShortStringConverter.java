package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author j.boesl, 04.03.15
 */
public class ShortStringConverter extends AbstractObjectConverter<Short>
{
  public ShortStringConverter()
  {
    super(Short.class);
    registerSourceTargetConverter(new SourceTargetConverter<Short, String>(String.class)
    {
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Short pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Short targetToSource(@NotNull String pTarget)
      {
        return Short.parseShort(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Short, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Short pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Short targetToSource(@NotNull Number pTarget)
      {
        return pTarget.shortValue();
      }
    });
  }
}
