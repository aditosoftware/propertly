package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 09.03.15
 */
public class StringStringConverter extends AbstractObjectConverter<String>
{
  public StringStringConverter()
  {
    super(String.class);
    registerSourceTargetConverter(new SourceTargetConverter<String, CharSequence>(CharSequence.class)
    {
      @Nonnull
      @Override
      public CharSequence sourceToTarget(@Nonnull String pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public String targetToSource(@Nonnull CharSequence pTarget)
      {
        return pTarget.toString();
      }
    });
  }
}
