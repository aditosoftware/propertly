package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author j.boesl, 04.03.15
 */
public class CharStringConverter extends AbstractObjectConverter<Character>
{
  public CharStringConverter()
  {
    super(Character.class);
    registerSourceTargetConverter(new SourceTargetConverter<Character, String>(String.class)
    {
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Character pSource)
      {
        return pSource == '\0' ? "" : pSource.toString();
      }

      @Nullable
      @Override
      public Character targetToSource(@NotNull String pTarget)
      {
        return pTarget.length() == 0 ? '\0' : pTarget.charAt(0);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Character, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Character pSource)
      {
        return (int) pSource;
      }

      @Nullable
      @Override
      public Character targetToSource(@NotNull Number pTarget)
      {
        return (char) pTarget.intValue();
      }
    });
  }
}
