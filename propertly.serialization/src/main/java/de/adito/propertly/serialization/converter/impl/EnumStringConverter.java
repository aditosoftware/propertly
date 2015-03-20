package de.adito.propertly.serialization.converter.impl;

import de.adito.propertly.serialization.converter.IObjectStringConverter;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class EnumStringConverter extends AbstractRegisterableConverter<Enum> implements IObjectStringConverter<Enum>
{
  @Nonnull
  @Override
  public Class<? super Enum> getCommonType()
  {
    return Enum.class;
  }

  @Nullable
  @Override
  public Enum stringToValue(@Nonnull String pValueAsString, @Nonnull Class<? extends Enum> pType)
  {
    return Enum.valueOf(pType, pValueAsString);
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull Enum pValue)
  {
    return pValue.name();
  }
}
