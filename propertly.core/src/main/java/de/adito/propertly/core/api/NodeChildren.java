package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author j.boesl, 09.11.14
 */
class NodeChildren implements Iterable<INode>
{
  private final Map<String, INode> childrenMap = new HashMap<>();
  private final List<INode> childrenList = new ArrayList<>();

  /**
   * Removes all elements from the underlying data structures, effectively clearing
   * the collection of child nodes. After calling this method, both the internal
   * map and list of child nodes will be empty.
   */
  public void clear()
  {
    childrenMap.clear();
    childrenList.clear();
  }

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
  public void add(@Nullable Integer pIndex, @NotNull INode pNode)
  {
    String name = pNode.getProperty().getName();
    if (!childrenMap.containsKey(name))
    {
      if (pIndex == null)
        childrenList.add(pNode);
      else
        childrenList.add(pIndex, pNode);
      childrenMap.put(name, pNode);
    }
  }

  /**
   * Removes the specified node from the collection of children nodes
   *
   * @param pNode the node to be removed
   * @return {@code true} if the node was successfully removed; {@code false} otherwise
   */
  public boolean remove(@NotNull INode pNode)
  {
    IProperty property = pNode.getProperty();
    boolean wasRemoved = childrenMap.remove(property.getName()) != null;
    if (wasRemoved)
      childrenList.remove(pNode);
    return wasRemoved;
  }

  /**
   * Removes the child node at the specified index
   *
   * @param pIndex the index of the child node to be removed
   */
  public void remove(int pIndex)
  {
    INode removedNode = childrenList.remove(pIndex);
    childrenMap.remove(removedNode.getProperty().getName());
  }

  /**
   * Renames a property within the internal structure, updating its associated name.
   * If a property with the specified new name already exists, an exception is thrown.
   *
   * @param pPropertyDescription the property description containing the original name of the property to be renamed
   * @param pName                the new name for the property
   * @throws RuntimeException if a property with the specified new name already exists
   */
  public void rename(@NotNull IPropertyDescription pPropertyDescription, @NotNull String pName)
  {
    if (childrenMap.containsKey(pName))
      throw new RuntimeException("property with name '" + pName + "' already exists.");
    INode node = childrenMap.remove(pPropertyDescription.getName());
    assert node != null;
    childrenMap.put(pName, node);
  }

  /**
   * Determines the index of a child node associated with the given property
   * description. This method locates the node corresponding to the given
   * property description and retrieves its position from the internal children list.
   *
   * @param pPropertyDescription the property description used to locate the associated child node
   * @return the index of the child node in the children list, or -1 if the node is not found
   */
  public int indexOf(@NotNull IPropertyDescription pPropertyDescription)
  {
    INode node = find(pPropertyDescription);
    return childrenList.indexOf(node);
  }

  /**
   * Reorders the list of child nodes based on the provided comparator.
   * The comparator is applied to the properties of the child nodes.
   *
   * @param pComparator the comparator used to reorder the child nodes based on their properties
   */
  public void reorder(@NotNull Comparator pComparator)
  {
    childrenList.sort((o1, o2) -> {
      //noinspection unchecked
      return pComparator.compare(o1.getProperty(), o2.getProperty());
    });
  }

  /**
   * Returns an unmodifiable view of the list of child nodes.
   *
   * @return an unmodifiable list of child nodes
   */
  @NotNull
  public List<INode> asList()
  {
    return Collections.unmodifiableList(childrenList);
  }

  /**
   * Finds a child node by its name.
   * If the node with the specified name exists, it returns that node; otherwise, it returns {@code null}.
   *
   * @param pName the name of the child node to search for
   * @return the child node with the specified name, or {@code null} if no such node exists.
   */
  @Nullable
  public INode find(@NotNull String pName)
  {
    return childrenMap.get(pName);
  }

  /**
   * Searches for a child node that matches the specified property description.
   *
   * @param pPropertyDescription the property description containing details about the expected child node,
   *                             including property name, type, and source type
   * @return the matching child node if found and its type and source type are compatible; otherwise {@code null}
   */
  @Nullable
  public INode find(@NotNull IPropertyDescription<?, ?> pPropertyDescription)
  {
    INode node = childrenMap.get(pPropertyDescription.getName());
    if (node == null)
      return null;
    boolean fittingTypeAndSourceType = pPropertyDescription.getType().isAssignableFrom(node.getProperty().getType()) &&
        pPropertyDescription.getSourceType().isAssignableFrom(node.getProperty().getDescription().getSourceType());
    return fittingTypeAndSourceType ? node : null;
  }

  /**
   * Retrieves the child node at the specified index in the collection of child nodes.
   *
   * @param pIndex the index of the child node to retrieve
   * @return the child node at the specified index
   */
  @NotNull
  public INode get(int pIndex)
  {
    return childrenList.get(pIndex);
  }

  @NotNull
  @Override
  public Iterator<INode> iterator()
  {
    return asList().iterator();
  }
}
