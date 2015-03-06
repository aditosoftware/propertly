package de.adito.propertly.core.common.serialization.converter.impl;

import de.adito.propertly.core.common.serialization.converter.*;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class EnumStringConverter implements ITypeStringConverter<Enum>, IObjectStringConverter<Enum>
{
  @Nonnull
  @Override
  public Class<? super Enum> getCommonType()
  {
    return Enum.class;
  }

  @Nullable
  @Override
  public Class<? extends Enum> stringToType(@Nonnull String pTypeAsString)
  {
    try
    {
      //noinspection unchecked
      return (Class<? extends Enum>) Class.forName(pTypeAsString);
    }
    catch (ClassNotFoundException e)
    {
      return null;
    }
  }

  @Nonnull
  @Override
  public String typeToString(@Nonnull Class<? extends Enum> pType)
  {
    return pType.getCanonicalName();
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
