package de.adito.propertly.serialization.converter.impl;

import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author j.boesl, 05.03.15
 */
public class TypePPPStringConverter extends AbstractSubTypeStringConverter<IPropertyPitProvider>
{
  @NotNull
  @Override
  public Class<IPropertyPitProvider> getCommonType()
  {
    return IPropertyPitProvider.class;
  }

  @Nullable
  @Override
  public IPropertyPitProvider targetToSource(@NotNull Object pTarget, @NotNull Class<? extends IPropertyPitProvider> pSourceType)
  {
    throw new RuntimeException("'targetToSource' not available for " + getClass().getSimpleName());
  }

  @NotNull
  @Override
  public Object sourceToTarget(@NotNull IPropertyPitProvider pSource, @NotNull Class... pTargetTypes)
  {
    throw new RuntimeException("'sourceToTarget' not available for " + getClass().getSimpleName());
  }
}
