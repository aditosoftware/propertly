package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class DoubleStringConverter extends AbstractObjectStringConverter<Double>
{
  public DoubleStringConverter()
  {
    super(Double.class);
    registerSourceTargetConverter(new SourceTargetConverter<Double, String>(String.class)
    {
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Double pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Double targetToSource(@Nonnull String pTarget)
      {
        return Double.parseDouble(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Double, Number>(Number.class)
    {
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Double pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Double targetToSource(@Nonnull Number pTarget)
      {
        return pTarget.doubleValue();
      }
    });
  }
}
