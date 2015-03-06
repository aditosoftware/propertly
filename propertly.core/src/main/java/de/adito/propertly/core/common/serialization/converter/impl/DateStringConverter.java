package de.adito.propertly.core.common.serialization.converter.impl;

import javax.annotation.*;
import java.text.*;
import java.util.Date;

/**
 * @author j.boesl, 04.03.15
 */
public class DateStringConverter extends AbstractObjectStringConverter<Date>
{
  private final SimpleDateFormat dateFormat;

  public DateStringConverter()
  {
    super(Date.class);
    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
  }

  @Nullable
  @Override
  protected Date stringToValue(@Nonnull String pValueAsString)
  {
    try
    {
      return dateFormat.parse(pValueAsString);
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Date pValue)
  {
    return dateFormat.format(pValue);
  }
}
