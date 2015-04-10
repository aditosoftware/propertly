package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class FloatStringConverter extends AbstractObjectConverter<Float>
{
  public FloatStringConverter()
  {
    super(Float.class);
    registerSourceTargetConverter(new SourceTargetConverter<Float, String>(String.class)
    {
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Float pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Float targetToSource(@Nonnull String pTarget)
      {
        return Float.parseFloat(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Float, Number>(Number.class)
    {
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Float pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Float targetToSource(@Nonnull Number pTarget)
      {
        return pTarget.floatValue();
      }
    });
  }
}
