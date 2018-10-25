package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Long pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Long targetToSource(@NotNull String pTarget)
      {
        return Long.parseLong(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Long, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Long pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Long targetToSource(@NotNull Number pTarget)
      {
        return pTarget.longValue();
      }
    });
  }
}
