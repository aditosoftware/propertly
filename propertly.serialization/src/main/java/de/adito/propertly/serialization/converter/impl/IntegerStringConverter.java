package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author j.boesl, 04.03.15
 */
public class IntegerStringConverter extends AbstractObjectConverter<Integer>
{
  public IntegerStringConverter()
  {
    super(Integer.class);
    registerSourceTargetConverter(new SourceTargetConverter<Integer, String>(String.class)
    {
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Integer pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Integer targetToSource(@NotNull String pTarget)
      {
        return Integer.parseInt(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Integer, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Integer pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Integer targetToSource(@NotNull Number pTarget)
      {
        return pTarget.intValue();
      }
    });
  }
}
