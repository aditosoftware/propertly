package de.adito.propertly.core.common.serialization.converter;

import javax.annotation.Nonnull;

/**
 * @author j.boesl, 05.03.15
 */
public interface IConverterTypeProvider<T>
{

  @Nonnull
  Class<? super T> getCommonType();

}
