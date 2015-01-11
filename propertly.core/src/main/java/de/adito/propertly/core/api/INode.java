package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitEventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

/**
 * INodes are the storage structure behind IProperty objects.
 *
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

  /**
   * Adds a weak listener.
   *
   * @param pListener the listener to be weakly added.
   */
  void addWeakListener(@Nonnull IPropertyPitEventListener pListener);

  /**
   * Adds a strong listener.
   *
   * @param pListener the listener to be strongly added.
   */
  void addStrongListener(@Nonnull IPropertyPitEventListener pListener);

  /**
   * Removes a listener.
   *
   * @param pListener the listener to be removed.
   */
  void removeListener(@Nonnull IPropertyPitEventListener pListener);

}
