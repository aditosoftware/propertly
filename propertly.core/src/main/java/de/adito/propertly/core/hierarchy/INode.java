package de.adito.propertly.core.hierarchy;

import de.adito.propertly.core.api.IProperty;
import de.adito.propertly.core.api.IPropertyDescription;
import de.adito.propertly.core.api.IPropertyEventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

/**
 * @author PaL
 *         Date: 06.04.13
 *         Time: 18:59
 */
public interface INode
{
  @Nonnull
  Hierarchy getHierarchy();

  @Nullable
  INode getParent();

  @Nullable
  List<INode> getChildren();

  @Nullable
  INode findNode(@Nonnull IPropertyDescription pPropertyDescription);

  @Nullable
  Object getValue();

  @Nullable
  Object setValue(@Nullable Object pValue);

  boolean canRead();

  boolean canWrite();

  @Nonnull
  String getPath();

  @Nonnull
  IProperty getProperty();

  void rename(String pName);

  void addProperty(@Nonnull IPropertyDescription pPropertyDescription);

  boolean removeProperty(@Nonnull IPropertyDescription pPropertyDescription);

  void addProperty(int pIndex, @Nonnull IPropertyDescription pPropertyDescription);

  void removeProperty(int pIndex);

  void reorder(Comparator pComparator);

  void addPropertyEventListener(@Nonnull IPropertyEventListener pListener);

  void removePropertyEventListener(@Nonnull IPropertyEventListener pListener);

}