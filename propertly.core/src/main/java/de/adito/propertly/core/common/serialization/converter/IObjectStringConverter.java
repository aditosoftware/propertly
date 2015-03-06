package de.adito.propertly.core.common.serialization.converter;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public interface IObjectStringConverter<T> extends IConverterTypeProvider<T>
{

  @Nullable
  T stringToValue(@Nonnull String pValueAsString, @Nonnull Class<? extends T> pType);

  @Nonnull
  String valueToString(@Nonnull T pValue);

}
