package de.adito.propertly.core.api;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 21:27
 */
public interface IIndexedMutablePropertyPit<P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
    extends IMutablePropertyPit<P, S, T>, IIndexedMutablePropertyPitProvider<P, S, T>
{

  int getSize();

  @Nonnull
  IProperty<S, T> getProperty(int pIndex);

  @Nonnull
  IProperty<S, T> addProperty(int pIndex, IPropertyDescription<S, T> pPropertyDescription);

  void removeProperty(int pIndex);

  void reorder(Comparator<IProperty<S, T>> pComparator);

}
