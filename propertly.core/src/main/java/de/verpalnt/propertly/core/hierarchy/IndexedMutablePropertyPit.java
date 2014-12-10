package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:42
 */
public class IndexedMutablePropertyPit<S extends IIndexedMutablePropertyPitProvider, T> extends MutablePropertyPit<S, T> implements IIndexedMutablePropertyPit<S, T>
{


  IndexedMutablePropertyPit(S pSource, Class<T> pAllowedChildType)
  {
    super(pSource, pAllowedChildType);
  }

  public static <S extends IIndexedMutablePropertyPitProvider<S, T>, T> IndexedMutablePropertyPit<S, T> create(S pCreateFor, Class<T> pAllowedChildType)
  {
    return new IndexedMutablePropertyPit<S, T>(pCreateFor, pAllowedChildType);
  }

  @Override
  public IIndexedMutablePropertyPit<S, T> getPit()
  {
    return this;
  }

  @Override
  public int getSize()
  {
    List<INode> children = getNode().getChildren();
    return children == null ? 0 : children.size();
  }

  @Nonnull
  @Override
  public IProperty<S, T> getProperty(int pIndex)
  {
    List<INode> children = getNode().getChildren();
    if (children != null && pIndex >= 0 && pIndex < children.size())
      return children.get(pIndex).getProperty();
    throw new IndexOutOfBoundsException("index '" + pIndex + "' >= size '" + getSize() + "'.");
  }

  @Nonnull
  @Override
  public IProperty<S, T> addProperty(int pIndex, IPropertyDescription<S, T> pPropertyDescription)
  {
    getNode().addProperty(pIndex, pPropertyDescription);
    return getProperty(pIndex);
  }

  @Override
  public void removeProperty(int pIndex)
  {
    getNode().removeProperty(pIndex);
  }

  @Override
  public void reorder(Comparator<IProperty<S, T>> pComparator)
  {
    getNode().reorder(pComparator);
  }
}
