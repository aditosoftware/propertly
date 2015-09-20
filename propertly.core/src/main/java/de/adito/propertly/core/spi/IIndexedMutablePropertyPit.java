package de.adito.propertly.core.spi;

import javax.annotation.*;
import java.util.Comparator;

/**
 * IIndexedMutablePropertyPit is an IMutablePropertyPit that has a defined order for it's properties and gives access
 * to them by index. Further it's IProperty objects can be reordered.
 *
 * @author PaL
 *         Date: 13.07.14
 *         Time. 21:27
 */
public interface IIndexedMutablePropertyPit<P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
    extends IMutablePropertyPit<P, S, T>, IIndexedMutablePropertyPitProvider<P, S, T>
{

  /**
   * @return the number of IProperty objects in this IIndexedMutablePropertyPit.
   */
  int getSize();

  /**
   * @param pIndex the index for an IProperty.
   * @return the IProperty at the given index.
   */
  @Nonnull
  IProperty<S, T> getProperty(int pIndex);

  /**
   * Adds an IProperty at a given index.
   *
   * @param pIndex               the index where the IProperty shall be inserted.
   * @param pPropertyDescription the IPropertyDescription describing the new IProperty.
   * @param pAttributes          additional attributes describing this change.
   * @return the created IProperty.
   */
  @Nonnull
  IProperty<S, T> addProperty(int pIndex, @Nonnull IPropertyDescription<S, T> pPropertyDescription, @Nullable Object... pAttributes);

  /**
   * @param pIndex      the index for the IProperty to be removed.
   * @param pAttributes additional attributes describing this change.
   */
  void removeProperty(int pIndex, @Nullable Object... pAttributes);

  /**
   * Returns the index of a property within this IIndexedMutablePropertyPit.
   *
   * @param pProperty the property for which the index is looked for.
   * @return the index of an child property. '-1' in case the property is not a child of this pit.
   */
  int indexOf(@Nonnull IProperty<?, ?> pProperty);

  /**
   * Reorders the IProperty objects in this IIndexedMutablePropertyPit.
   *
   * @param pComparator is used for oredering.
   * @param pAttributes additional attributes describing this change.
   */
  void reorder(@Nonnull Comparator<IProperty<S, T>> pComparator, @Nullable Object... pAttributes);

}
