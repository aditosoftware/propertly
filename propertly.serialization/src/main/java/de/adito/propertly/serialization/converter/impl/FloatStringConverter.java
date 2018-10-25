package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Float pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Float targetToSource(@NotNull String pTarget)
      {
        return Float.parseFloat(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Float, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Float pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Float targetToSource(@NotNull Number pTarget)
      {
        return pTarget.floatValue();
      }
    });
  }
}
