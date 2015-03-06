package de.adito.propertly.core.common.serialization.converter;

import javax.annotation.*;

/**
 * @author j.boesl, 05.03.15
 */
public interface ITypeStringConverter<T> extends IConverterTypeProvider<T>
{

  @Nullable
  Class<? extends T> stringToType(@Nonnull String pTypeAsString);

  @Nonnull
  String typeToString(@Nonnull Class<? extends T> pType);

}
