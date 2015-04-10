package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

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
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Short pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Short targetToSource(@Nonnull String pTarget)
      {
        return Short.parseShort(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Short, Number>(Number.class)
    {
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Short pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Short targetToSource(@Nonnull Number pTarget)
      {
        return pTarget.shortValue();
      }
    });
  }
}
