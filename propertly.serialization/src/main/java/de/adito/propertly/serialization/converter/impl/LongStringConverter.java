package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class LongStringConverter extends AbstractObjectConverter<Long>
{
  public LongStringConverter()
  {
    super(Long.class);
    registerSourceTargetConverter(new SourceTargetConverter<Long, String>(String.class)
    {
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Long pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Long targetToSource(@Nonnull String pTarget)
      {
        return Long.parseLong(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Long, Number>(Number.class)
    {
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Long pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Long targetToSource(@Nonnull Number pTarget)
      {
        return pTarget.longValue();
      }
    });
  }
}
