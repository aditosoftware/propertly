package de.verpalnt.propertly.core.api;

import javax.annotation.Nonnull;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 21:27
 */
public interface IIndexedMutablePropertyPit<S extends IIndexedMutablePropertyPitProvider, T> extends IMutablePropertyPit<S, T>, IMutablePropertyPitProvider<S, T>
{

  int getSize();

  @Nonnull
  IProperty<S, T> getProperty(int pIndex);

  @Nonnull
  IProperty<S, T> addProperty(int pIndex, IPropertyDescription<S, T> pPropertyDescription);

  void removeProperty(int pIndex);

}
