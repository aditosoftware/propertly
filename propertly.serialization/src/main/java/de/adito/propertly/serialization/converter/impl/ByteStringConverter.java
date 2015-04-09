package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class ByteStringConverter extends AbstractObjectStringConverter<Byte>
{
  public ByteStringConverter()
  {
    super(Byte.class);
    registerSourceTargetConverter(new SourceTargetConverter<Byte, String>(String.class)
    {
      @Nonnull
      @Override
      public String sourceToTarget(@Nonnull Byte pSource)
      {
        return pSource.toString();
      }

      @Nullable
      @Override
      public Byte targetToSource(@Nonnull String pTarget)
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
      @Nonnull
      @Override
      public Number sourceToTarget(@Nonnull Byte pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public Byte targetToSource(@Nonnull Number pTarget)
      {
        return pTarget.byteValue();
      }
    });
  }
}
