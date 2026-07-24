package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author j.boesl, 09.11.14
 */
public abstract class AbstractNodeChildren implements Iterable<INode>
{
  /**
   * Removes all elements from the underlying data structures, effectively clearing
   * the collection of child nodes.
   */
  public abstract void clear();

  /**
   * Adds a child node to the current node structure at the specified index.
   * If the index is null, the new node is appended at the end of the children list.
   *
   * @param pNode the child node to be added
   */
  public void add(@NotNull INode pNode)
  {
    add(null, pNode);
  }

  /**
   * Adds a child node to the current node structure at the specified index or appends it at
   * the end if the index is null.
   *
   * @param pIndex the numerical index where the child node should be inserted, or null to add
   *               the node at the end of the children list
   * @param pNode  the child node to be added
   */
  public abstract void add(@Nullable Integer pIndex, @NotNull INode pNode);

  /**
   * Removes the specified node from the collection of children nodes
   *
   * @param pNode the node to be removed
   * @return {@code true} if the node was successfully removed; {@code false} otherwise
   */
  public abstract boolean remove(@NotNull INode pNode);

  /**
   * Removes the child node at the specified index
   *
   * @param pIndex the index of the child node to be removed
   */
  public abstract void remove(int pIndex);

  /**
   * Renames a property within the internal structure, updating its associated name.
   * If a property with the specified new name already exists, an exception is thrown.
   *
   * @param pPropertyDescription the property description containing the original name of the property to be renamed
   * @param pName                the new name for the property
   * @throws RuntimeException if a property with the specified new name already exists
   */
  public abstract void rename(@NotNull IPropertyDescription pPropertyDescription, @NotNull String pName);

  /**
   * Determines the index of a child node associated with the given property
   * description.
   *
   * @param pPropertyDescription the property description used to locate the associated child node
   * @return the index of the child node in the children list, or -1 if the node is not found
   */
  public abstract int indexOf(@NotNull IPropertyDescription pPropertyDescription);

  /**
   * Reorders the list of child nodes based on the provided comparator.
   *
   * @param pComparator the comparator used to reorder the child nodes based on their properties
   */
  public abstract void reorder(@NotNull Comparator pComparator);

  /**
   * Returns an unmodifiable view of the list of child nodes.
   *
   * @return an unmodifiable list of child nodes
   */
  @NotNull
  public abstract List<INode> asList();

  /**
   * Finds a child node by its name.
   *
   * @param pName the name of the child node to search for
   * @return the child node with the specified name, or {@code null} if no such node exists.
   */
  @Nullable
  public abstract INode find(@NotNull String pName);

  /**
   * Searches for a child node that matches the specified property description.
   *
   * @param pPropertyDescription the property description containing details about the expected child node
   * @return the matching child node if found and its type and source type are compatible; otherwise {@code null}
   */
  @Nullable
  public abstract INode find(@NotNull IPropertyDescription<?, ?> pPropertyDescription);

  /**
   * Retrieves the child node at the specified index in the collection of child nodes.
   *
   * @param pIndex the index of the child node to retrieve
   * @return the child node at the specified index
   */
  @NotNull
  public abstract INode get(int pIndex);

  @NotNull
  @Override
  public abstract Iterator<INode> iterator();
}
