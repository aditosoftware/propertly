package de.adito.propertly.core.common.serialization.converter.impl;

import de.adito.propertly.core.common.serialization.converter.ITypeStringConverter;
import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.*;

/**
 * @author j.boesl, 05.03.15
 */
public class TypePPPStringConverter implements ITypeStringConverter<IPropertyPitProvider>
{
  @Nonnull
  @Override
  public Class<? super IPropertyPitProvider> getCommonType()
  {
    return IPropertyPitProvider.class;
  }

  @Nullable
  @Override
  public Class<? extends IPropertyPitProvider> stringToType(@Nonnull String pTypeAsString)
  {
    try
    {
      Class<?> cls = Class.forName(pTypeAsString);
      if (IPropertyPitProvider.class.isAssignableFrom(cls))
      {
        //noinspection unchecked
        return (Class<? extends IPropertyPitProvider>) cls;
      }
      return null;
    }
    catch (ClassNotFoundException e)
    {
      return null;
    }
  }

  @Nonnull
  @Override
  public String typeToString(@Nonnull Class<? extends IPropertyPitProvider> pType)
  {
    return pType.getCanonicalName();
  }
}
