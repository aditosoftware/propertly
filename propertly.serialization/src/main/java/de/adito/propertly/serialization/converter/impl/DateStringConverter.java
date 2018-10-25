package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author j.boesl, 04.03.15
 */
public class DateStringConverter extends AbstractObjectConverter<Date>
{
  private final SimpleDateFormat dateFormat;

  public DateStringConverter()
  {
    super(Date.class);
    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
    registerSourceTargetConverter(new SourceTargetConverter<Date, String>(String.class)
    {
      @NotNull
      @Override
      public String sourceToTarget(@NotNull Date pSource)
      {
        return dateFormat.format(pSource);
      }

      @Nullable
      @Override
      public Date targetToSource(@NotNull String pTarget)
      {
        try
        {
          return dateFormat.parse(pTarget);
        }
        catch (ParseException e)
        {
          throw new RuntimeException(e);
        }
      }
    });
    registerSourceTargetConverter(new SourceTargetConverter<Date, Number>(Number.class)
    {
      @NotNull
      @Override
      public Number sourceToTarget(@NotNull Date pSource)
      {
        return pSource.getTime();
      }

      @Nullable
      @Override
      public Date targetToSource(@NotNull Number pTarget)
      {
        return new Date(pTarget.longValue());
      }
    });
  }
}
