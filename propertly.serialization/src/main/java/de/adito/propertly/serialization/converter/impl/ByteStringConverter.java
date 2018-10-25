package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author j.boesl, 04.03.15
 */
public class ByteStringConverter extends AbstractObjectConverter<Byte>
{
  public ByteStringConverter()
  {
    super(Byte.class);
    registerSourceTargetConverter(new SourceTargetConverter<Byte, String>(String.class)
    {
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Byte pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Byte targetToSource(@NotNull String pTarget)
      {
        int value = Integer.decode(pTarget);
        if (value >= -128 && value <= 255)
        {
          return (byte) value;
        }
        else
        {
          throw new NumberFormatException("For input string: \"" + pTarget + '\"');
        }
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Byte, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Byte pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Byte targetToSource(@NotNull Number pTarget)
      {
        return pTarget.byteValue();
      }
    });
  }
}
