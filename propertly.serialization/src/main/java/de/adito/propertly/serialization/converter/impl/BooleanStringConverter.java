package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class BooleanStringConverter extends AbstractObjectConverter<Boolean>
{
  public BooleanStringConverter()
  {
    super(Boolean.class);
    registerSourceTargetConverter(new SourceTargetConverter<Boolean, String>(String.class)
    {
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Boolean pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Boolean targetToSource(@Nonnull String pTarget)
      {
        return "true".equalsIgnoreCase(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Boolean, Number>(Number.class)
    {
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Boolean pSource)
      {
        return Boolean.TRUE.equals(pSource) ? 0x1 : 0x0;
      }

      @Nullable
      @Override
      public Boolean targetToSource(@Nonnull Number pTarget)
      {
        return pTarget.byteValue() != 0;
      }
    });
  }

}
