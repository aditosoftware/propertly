package de.adito.propertly.serialization.converter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author j.boesl, 04.03.15
 */
public class EnumStringConverter extends AbstractSubTypeStringConverter<Enum>
{
  @NotNull
  @Override
  public Class<Enum> getCommonType()
  {
    return Enum.class;
  }

  @Nullable
  @Override
  public Enum targetToSource(@NotNull Object pTarget, @NotNull Class<? extends Enum> pSourceType)
  {
    if (pTarget instanceof String)
      return Enum.valueOf(pSourceType, (String) pTarget);
    throw new IllegalArgumentException(pTarget.getClass() + " is not convertible to " + pSourceType + ".");
  }

  @NotNull
  @Override
  public Object sourceToTarget(@NotNull Enum pSource, @NotNull Class... pTargetTypes)
  {
    for (Class targetType : pTargetTypes)
      if (targetType == String.class)
        return pSource.name();
    throw new IllegalArgumentException("no supported target type for " + getCommonType() + ".");
  }
}
