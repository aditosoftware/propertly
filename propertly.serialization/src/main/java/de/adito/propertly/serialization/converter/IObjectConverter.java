package de.adito.propertly.serialization.converter;

import javax.annotation.*;

/**
 * @author j.boesl, 05.03.15
 */
public interface IObjectConverter<S>
{

  @Nonnull
  Class<S> getCommonType();

  @Nullable
  Class<? extends S> stringToType(@Nonnull String pTypeAsString);

  @Nonnull
  String typeToString(@Nonnull Class<? extends S> pType);

  @Nullable
  S targetToSource(@Nonnull Object pTarget, @Nonnull Class<? extends S> pSourceType);

  @Nonnull
  Object sourceToTarget(@Nonnull S pSource, @Nonnull Class... pTargetTypes);

}
