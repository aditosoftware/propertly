package de.adito.propertly.serialization.converter.impl;

import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.*;

/**
 * @author j.boesl, 05.03.15
 */
public class TypePPPStringConverter extends AbstractSubTypeStringConverter<IPropertyPitProvider>
{
  @Nonnull
  @Override
  public Class<IPropertyPitProvider> getCommonType()
  {
    return IPropertyPitProvider.class;
  }

  @Nullable
  @Override
  public IPropertyPitProvider targetToSource(@Nonnull Object pTarget, @Nonnull Class<? extends IPropertyPitProvider> pSourceType)
  {
    throw new RuntimeException("'targetToSource' not available for " + getClass().getSimpleName());
  }

  @Nonnull
  @Override
  public Object sourceToTarget(@Nonnull IPropertyPitProvider pSource, @Nonnull Class... pTargetTypes)
  {
    throw new RuntimeException("'sourceToTarget' not available for " + getClass().getSimpleName());
  }
}
