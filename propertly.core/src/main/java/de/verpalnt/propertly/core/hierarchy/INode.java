package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.*;
import java.util.*;

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

  @Nonnull
  String getPath();

  @Nonnull
  IProperty getProperty();

  void rename(String pName);

  void addProperty(@Nonnull IPropertyDescription pPropertyDescription);

  boolean removeProperty(@Nonnull IPropertyDescription pPropertyDescription);

  void addProperty(int pIndex, @Nonnull IPropertyDescription pPropertyDescription);

  void removeProperty(int pIndex);

  void reorder(Comparator<Object> pComparator);

  void addPropertyEventListener(@Nonnull IPropertyEventListener pListener);

  void removePropertyEventListener(@Nonnull IPropertyEventListener pListener);


}
