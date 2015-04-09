package de.adito.propertly.serialization.converter.impl;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public class EnumStringConverter extends AbstractSubTypeStringConverter<Enum>
{
  @Nonnull
  @Override
  public Class<Enum> getCommonType()
  {
    return Enum.class;
  }

  @Nullable
  @Override
  public Enum targetToSource(@Nonnull Object pTarget, @Nonnull Class<? extends Enum> pSourceType)
  {
    if (pTarget instanceof String)
      return Enum.valueOf(pSourceType, (String) pTarget);
    throw new IllegalArgumentException(pTarget.getClass() + " is not convertible to " + pSourceType + ".");
  }

  @Nonnull
  @Override
  public Object sourceToTarget(@Nonnull Enum pSource, @Nonnull Class... pTargetTypes)
  {
    for (Class targetType : pTargetTypes)
      if (targetType == String.class)
        return pSource.name();
    throw new IllegalArgumentException("no supported target type for " + getCommonType() + ".");
  }
}
