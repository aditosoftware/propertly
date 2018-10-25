package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Boolean pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Boolean targetToSource(@NotNull String pTarget)
      {
        return "true".equalsIgnoreCase(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Boolean, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Boolean pSource)
      {
        return Boolean.TRUE.equals(pSource) ? 0x1 : 0x0;
      }

      @Nullable
      @Override
      public Boolean targetToSource(@NotNull Number pTarget)
      {
        return pTarget.byteValue() != 0;
      }
    });
  }

}
