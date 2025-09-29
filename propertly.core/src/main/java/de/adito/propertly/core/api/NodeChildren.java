package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author j.boesl, 09.11.14
 */
class NodeChildren implements Iterable<INode>
{
  private final Map<String, Integer> childrenIndexMap = new HashMap<>();
  private final AtomicBoolean childrenIndexMapDirty = new AtomicBoolean(true);

  private final List<INode> childrenList = new ArrayList<>();

  /**
   * Clears the internal state of child nodes by removing all entries from
   * the mapping of child node indices and the list of child nodes.
   * <p>
   * After invoking this method, the NodeChildren object will be in an empty state,
   * with no child nodes or index mappings remaining.
   */
  public void clear()
  {
    childrenIndexMap.clear();
    childrenList.clear();
    childrenIndexMapDirty.set(true);
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
    if (!childrenIndexMap.containsKey(name))
    {
      if (pIndex == null)
        childrenList.add(pNode);
      else
        childrenList.add(pIndex, pNode);

      childrenIndexMap.put(name, pIndex == null ? childrenList.size() - 1 : pIndex);
      childrenIndexMapDirty.set(true);
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
    boolean wasRemoved = childrenIndexMap.remove(property.getName()) != null;
    if (wasRemoved)
      childrenList.remove(pNode);
    childrenIndexMapDirty.set(true);
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
    childrenIndexMap.remove(removedNode.getProperty().getName());
    childrenIndexMapDirty.set(true);
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
    if (childrenIndexMap.containsKey(pName))
      throw new RuntimeException("property with name '" + pName + "' already exists.");
    Integer index = childrenIndexMap.remove(pPropertyDescription.getName());
    assert index != null;
    childrenIndexMap.put(pName, index);
  }

  /**
   * Returns the index of a child node that corresponds to the given property description.
   * If the internal index mapping is outdated, it recalculates the index map before performing the lookup.
   *
   * @param pPropertyDescription the property description used to locate the index of the corresponding child node
   * @return the index of the child node that matches the given property description, or -1 if no matching child exists
   */
  public int indexOf(@NotNull IPropertyDescription pPropertyDescription)
  {
    return indexOf(pPropertyDescription.getName());
  }

  /**
   * Returns the index of the child node with the specified name.
   * If the mapping is outdated, it recalculates the index map of child nodes before performing the lookup.
   *
   * @param pName the name of the child node whose index is to be retrieved
   * @return the index of the child node with the specified name, or -1 if no such child exists
   */
  private int indexOf(@NotNull String pName)
  {
    if (childrenIndexMapDirty.getAndSet(false))
    {
      childrenIndexMap.clear();
      for (int i = 0; i < childrenList.size(); i++)
      {
        INode node = childrenList.get(i);
        childrenIndexMap.put(node.getProperty().getName(), i);
      }
    }

    return childrenIndexMap.getOrDefault(pName, -1);
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

    childrenIndexMapDirty.set(true);
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
    int idx = indexOf(pName);
    return idx == -1 ? null : get(idx);
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
    INode node = find(pPropertyDescription.getName());
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
