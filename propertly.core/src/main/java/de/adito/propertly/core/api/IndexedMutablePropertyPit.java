package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:42
 */
class IndexedMutablePropertyPit<P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
    extends MutablePropertyPit<P, S, T>
    implements IIndexedMutablePropertyPit<P, S, T>
{


  IndexedMutablePropertyPit(S pSource, Class<T> pAllowedChildType)
  {
    super(pSource, pAllowedChildType);
  }

  public static <P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
  IndexedMutablePropertyPit<P, S, T> create(S pCreateFor, Class<T> pAllowedChildType)
  {
    return new IndexedMutablePropertyPit<>(pCreateFor, pAllowedChildType);
  }

  @NotNull
  @Override
  public IIndexedMutablePropertyPit<P, S, T> getPit()
  {
    return this;
  }

  @Override
  public int getSize()
  {
    List<INode> children = getNode().getChildren();
    return children == null ? 0 : children.size();
  }

  @NotNull
  @Override
  public IProperty<S, T> getProperty(int pIndex)
  {
    List<INode> children = getNode().getChildren();
    if (children != null && pIndex >= 0 && pIndex < children.size())
      //noinspection unchecked
      return children.get(pIndex).getProperty();
    throw new IndexOutOfBoundsException("index '" + pIndex + "' >= size '" + getSize() + "'.");
  }

  @NotNull
  @Override
  public IProperty<S, T> addProperty(int pIndex, @NotNull IPropertyDescription<S, T> pPropertyDescription, @Nullable Object... pAttributes)
  {
    getNode().addProperty(pIndex, pPropertyDescription, PropertlyUtility.toNonnullSet(pAttributes));
    return getProperty(pIndex);
  }

  @Override
  public void removeProperty(int pIndex, @Nullable Object... pAttributes)
  {
    getNode().removeProperty(pIndex, PropertlyUtility.toNonnullSet(pAttributes));
  }

  @Override
  public int indexOf(@NotNull IPropertyDescription<?, ?> pPropertyDescription)
  {
    return getNode().indexOf(pPropertyDescription);
  }

  @Override
  public void reorder(@NotNull Comparator<IProperty<S, T>> pComparator, @Nullable Object... pAttributes)
  {
    getNode().reorder(pComparator, PropertlyUtility.toNonnullSet(pAttributes));
  }
}
