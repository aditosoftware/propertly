package de.adito.propertly.core.api;

import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.IHierarchy;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitEventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
   * @param pName the name of the searched child.
   * @return the found child node or <tt>null</tt> if a child node with the given description does not exist.
   */
  @Nullable
  INode findNode(@Nonnull String pName);

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
   * @param pValue      the value to be set.
   * @param pAttributes additional attributes describing this change.
   * @return the value that is set.
   */
  @Nullable
  Object setValue(@Nullable Object pValue, @Nonnull Set<Object> pAttributes);

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
   * The node is live and valid when it is connected to an IHierarchy.
   *
   * @return whether this INode is valid.
   */
  boolean isValid();

  /**
   * Removes this INode from the node hierarchy thus rendering it invalid.
   */
  void remove();

  /**
   * @return the IProperty object for this INode.
   */
  @Nonnull
  IProperty getProperty();

  /**
   * Tries to rename this INode. This usually is only possible nodes with an dynamic IProperty.
   *
   * @param pName       the new name for this node.
   * @param pAttributes additional attributes describing this change.
   * @throws PropertlyRenameException in case renaming fails.
   */
  void rename(@Nonnull String pName, @Nonnull Set<Object> pAttributes) throws PropertlyRenameException;

  /**
   * Adds a new child with a dynamic property at a specified index to this node. If the index is <tt>null</tt> the child
   * is appended.
   *
   * @param pIndex               the index where the new child node shall be inserted or <tt>null</tt>.
   * @param pPropertyDescription the description for the new node.
   * @param pAttributes          additional attributes describing this change.
   * @return the created property's node.
   */
  INode addProperty(@Nullable Integer pIndex, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes);

  /**
   * Removes a child from this node.
   *
   * @param pPropertyDescription describes the node to be removed.
   * @param pAttributes          additional attributes describing this change.
   * @return whether something was removed.
   */
  boolean removeProperty(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes);

  /**
   * Removes a property at a specified index. If no exception occurs the removal was successful.
   *
   * @param pIndex      the index at which the child node shall be removed.
   * @param pAttributes additional attributes describing this change.
   */
  void removeProperty(int pIndex, @Nonnull Set<Object> pAttributes);

  /**
   * @param pPropertyDescription the description for the property for which the index is looked for.
   * @return the index of an child property. '-1' in case the property is not a child of this node.
   */
  int indexOf(@Nonnull IPropertyDescription pPropertyDescription);

  /**
   * Reorders the child nodes using the given comparator.
   *
   * @param pComparator the comparator used to order the children.
   * @param pAttributes additional attributes describing this change.
   */
  void reorder(@Nonnull Comparator pComparator, @Nonnull Set<Object> pAttributes);

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
