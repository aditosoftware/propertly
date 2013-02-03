package de.verpalnt.propertly.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An IMutablePropertyPit allows to modify the available properties it provides.
 *
 * @author PaL
 *         Date: 18.11.12
 *         Time: 15:44
 */
public interface IMutablePropertyPit<S extends IMutablePropertyPitProvider, T> extends IPropertyPit<S>, IMutablePropertyPitProvider<S, T>
{

  @Nonnull
  <T> IProperty<? super S, T> addProperty(IPropertyDescription<? super S, T> pPropertyDescription);

  @Nullable
  <T> IProperty<? super S, T> removeProperty(IPropertyDescription<? super S, T> pPropertyDescription);

}
