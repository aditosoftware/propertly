package de.adito.propertly.serialization.converter.impl;

import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.Nonnull;

/**
 * @author j.boesl, 05.03.15
 */
public class TypePPPStringConverter extends AbstractRegisterableConverter<IPropertyPitProvider>
{
  @Nonnull
  @Override
  public Class<? super IPropertyPitProvider> getCommonType()
  {
    return IPropertyPitProvider.class;
  }
}
