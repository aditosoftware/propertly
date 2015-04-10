package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

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
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Integer pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Integer targetToSource(@Nonnull String pTarget)
      {
        return Integer.parseInt(pTarget);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Integer, Number>(Number.class)
    {
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Integer pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Integer targetToSource(@Nonnull Number pTarget)
      {
        return pTarget.intValue();
      }
    });
  }
}
