package de.adito.propertly.serialization.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author j.boesl, 05.03.15
 */
public interface IObjectConverter<S>
{

  @NotNull
  Class<S> getCommonType();

  @Nullable
  Class<? extends S> stringToType(@NotNull String pTypeAsString);

  @NotNull
  String typeToString(@NotNull Class<? extends S> pType);

  @Nullable
  S targetToSource(@NotNull Object pTarget, @NotNull Class<? extends S> pSourceType);

  @NotNull
  Object sourceToTarget(@NotNull S pSource, @NotNull Class... pTargetTypes);

}
