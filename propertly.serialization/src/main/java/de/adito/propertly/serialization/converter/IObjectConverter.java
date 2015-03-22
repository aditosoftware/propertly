package de.adito.propertly.serialization.converter;

import javax.annotation.*;

/**
 * @author j.boesl, 05.03.15
 */
public interface IObjectConverter<T>
{

  @Nonnull
  Class<T> getCommonType();

  @Nullable
  Class<? extends T> stringToType(@Nonnull String pTypeAsString);

  @Nonnull
  String typeToString(@Nonnull Class<? extends T> pType);

  @Nullable
  T stringToValue(@Nonnull String pValueAsString, @Nonnull Class<? extends T> pType);

  @Nonnull
  String valueToString(@Nonnull T pValue);

}
