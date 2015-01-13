package de.adito.propertly.core.api;

import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.*;

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

  /**
   * Each INode must have a Hierarchy object.
   *
   * @return the assigned hierarchy.
   */
  @Nonnull
  IHierarchy getHierarchy();

  /**
   * Gives access to the parent.
   *
   * @return the parental INode. In case this is the root node it is <tt>null</tt>.
   */
  @Nullable
  INode getParent();

  /**
   * Lists the children nodes.
   *
   * @return a list of children nodes. This is <tt>null</tt> in case this node is a leaf.
   */
  @Nullable
  List<INode> getChildren();

  /**
   * Tries to find a child node.
   *
   * @param pPropertyDescription describes the search children.
   * @return the found child node or <tt>null</tt> if a child node with the given description does not exist.
   */
  @Nullable
  INode findNode(@Nonnull IPropertyDescription pPropertyDescription);

  /**
   * The set value.
   *
   * @return this INode's current value. If this INode is not a leaf it's an instance of <tt>IPropertyPitProvider</tt>.
   */
  @Nullable
  Object getValue();

  /**
   * Sets the current value. The value that is set afterwards is returned thus one shall proceed with the returned
   * value.
   *
   * @param pValue the value to be set.
   * @return the value that is set.
   */
  @Nullable
  Object setValue(@Nullable Object pValue);

  /**
   * In some cases it might happen that the value can not be read.
   *
   * @return whether the value can be read from this INode.
   */
  boolean canRead();

  /**
   * In some cases it might happen that the value can not be written.
   *
   * @return whether the value can be written to this INode.
   */
  boolean canWrite();

  /**
   * @return the path from the root to this INode as a string separated with slashes ('/').
   */
  @Nonnull
  String getPath();

  /**
   * @return the IProperty object for this INode.
   */
  @Nonnull
  IProperty getProperty();

  /**
   * Tries to rename this INode. This usually is only possible nodes with an dynamic IProperty.
   *
   * @param pName the new name for this node.
   * @throws PropertlyRenameException in case renaming fails.
   */
  void rename(String pName) throws PropertlyRenameException;

  /**
   * Adds a new child with a dynamic property to this node.
   *
   * @param pPropertyDescription the description for the new node.
   */
  void addProperty(@Nonnull IPropertyDescription pPropertyDescription);

  /**
   * Removes a child from this node.
   *
   * @param pPropertyDescription describes the node to be removed.
   * @return whether something was removed.
   */
  boolean removeProperty(@Nonnull IPropertyDescription pPropertyDescription);

  /**
   * Add a new child with a dynamic property at a specified index to this node.
   *
   * @param pIndex               the index where the new child node shall be inserted.
   * @param pPropertyDescription the description for the new node.
   */
  void addProperty(int pIndex, @Nonnull IPropertyDescription pPropertyDescription);

  /**
   * Removes a property at a specified index. If no exception occurs the removal was successful.
   *
   * @param pIndex the index at which the child node shall be removed.
   */
  void removeProperty(int pIndex);

  /**
   * Reorders the child nodes using the given comparator.
   *
   * @param pComparator the comparator used to order the children.
   */
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
