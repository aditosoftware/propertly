package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IIndexedMutablePropertyPit;
import de.verpalnt.propertly.core.api.IIndexedMutablePropertyPitProvider;
import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author PaL
 *         Date: 13.07.14
 *         Time. 22:42
 */
public class IndexedMutablePropertyPit<S extends IIndexedMutablePropertyPitProvider, T> extends MutablePropertyPit<S, T> implements IIndexedMutablePropertyPit<S, T>
{


  public IndexedMutablePropertyPit(S pSource, Class<T> pAllowedChildType)
  {
    super(pSource, pAllowedChildType);
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
}
