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
  public IPropertyPitProvider stringToValue(@Nonnull String pValueAsString, @Nonnull Class<? extends IPropertyPitProvider> pType)
  {
    throw new RuntimeException("'stringToValue' not available for " + getClass().getSimpleName());
  }

  @Nonnull
  @Override
  public String valueToString(@Nonnull IPropertyPitProvider pValue)
  {
    throw new RuntimeException("'valueToString' not available for " + getClass().getSimpleName());
  }
}
