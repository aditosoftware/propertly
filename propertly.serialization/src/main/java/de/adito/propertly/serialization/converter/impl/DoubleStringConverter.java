package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author j.boesl, 04.03.15
 */
public class DoubleStringConverter extends AbstractObjectConverter<Double>
{
  public DoubleStringConverter()
  {
    super(Double.class);
    registerSourceTargetConverter(new SourceTargetConverter<Double, String>(String.class)
    {
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Double pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Double targetToSource(@NotNull String pTarget)
      {
        return Double.parseDouble(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Double, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Double pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Double targetToSource(@NotNull Number pTarget)
      {
        return pTarget.doubleValue();
      }
    });
  }
}
