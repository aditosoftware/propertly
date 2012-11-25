package de.verpalnt.propertly.listener;

import de.verpalnt.propertly.IProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author PaL
 *         Date: 13.11.12
 *         Time: 20:17
 */
public interface IPropertyEvent<S, T>
{

  @Nonnull
  IProperty<S, T> getProperty();

  @Nullable
  T oldValue();

  @Nullable
  T newValue();

}
