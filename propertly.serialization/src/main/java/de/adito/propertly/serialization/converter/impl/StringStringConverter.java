package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      @NotNull
      @Override
      public CharSequence sourceToTarget(@NotNull String pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public String targetToSource(@NotNull CharSequence pTarget)
      {
        return pTarget.toString();
      }
    });
  }
}
