package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class CharStringConverter extends AbstractObjectStringConverter<Character>
{
  public CharStringConverter()
  {
    super(Character.class);
    registerSourceTargetConverter(new SourceTargetConverter<Character, String>(String.class)
    {
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Character pSource)
      {
        return pSource == '\0' ? "" : pSource.toString();
      }

      @Nullable
      @Override
      public Character targetToSource(@Nonnull String pTarget)
      {
        return pTarget.length() == 0 ? '\0' : pTarget.charAt(0);
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Character, Number>(Number.class)
    {
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Character pSource)
      {
        return (int) pSource;
      }

      @Nullable
      @Override
      public Character targetToSource(@Nonnull Number pTarget)
      {
        return (char) pTarget.intValue();
      }
    });
  }
}
