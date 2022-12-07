package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author j.boesl, 09.11.14
 */
class NodeChildren implements Iterable<INode>
{

  private Map<String, INode> childrenMap;
  private List<INode> childrenList;

  public NodeChildren()
  {
    childrenMap = new HashMap<>();
    childrenList = new ArrayList<>();
  }

  public void clear()
  {
    childrenMap.clear();
    childrenList.clear();
  }

  public void add(INode pNode)
  {
    add(null, pNode);
  }

  public void add(Integer pIndex, INode pNode)
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

  public boolean remove(INode pNode)
  {
    IProperty property = pNode.getProperty();
    boolean wasRemoved = childrenMap.remove(property.getName()) != null;
    if (wasRemoved)
      childrenList.remove(pNode);
    return wasRemoved;
  }

  public void remove(int pIndex)
  {
    INode removedNode = childrenList.remove(pIndex);
    childrenMap.remove(removedNode.getProperty().getName());
  }

  public void rename(IPropertyDescription pPropertyDescription, String pName)
  {
    if (childrenMap.containsKey(pName))
      throw new RuntimeException("property with name '" + pName + "' already exists.");
    INode node = childrenMap.remove(pPropertyDescription.getName());
    assert node != null;
    childrenMap.put(pName, node);
  }

  public int indexOf(IPropertyDescription pPropertyDescription)
  {
    INode node = find(pPropertyDescription);
    return childrenList.indexOf(node);
  }

  public void reorder(final Comparator pComparator)
  {
    Collections.sort(childrenList, (o1, o2) -> {
      //noinspection unchecked
      return pComparator.compare(o1.getProperty(), o2.getProperty());
    });
  }

  public List<INode> asList()
  {
    return Collections.unmodifiableList(childrenList);
  }

  /**
   * Finds the nodes ignoring the case of the given name
   *
   * @param pName the name for searching
   * @return the found node or {@code null}
   */
  @Nullable
  public INode findIgnoringCase(@NotNull String pName)
  {
    return childrenMap.entrySet().stream()
        .filter(pEntry -> pEntry.getKey().equalsIgnoreCase(pName))
        .map(Map.Entry::getValue)
        .findFirst()
        .orElse(null);
  }

  public INode find(String pName)
  {
    return childrenMap.get(pName);
  }

  public INode find(IPropertyDescription<?, ?> pPropertyDescription)
  {
    INode node = childrenMap.get(pPropertyDescription.getName());
    if (node == null)
      return null;
    boolean fittingTypeAndSourceType = pPropertyDescription.getType().isAssignableFrom(node.getProperty().getType()) &&
        pPropertyDescription.getSourceType().isAssignableFrom(node.getProperty().getDescription().getSourceType());
    return fittingTypeAndSourceType ? node : null;
  }

  public INode get(int pIndex)
  {
    return childrenList.get(pIndex);
  }

  @Override
  public Iterator<INode> iterator()
  {
    return asList().iterator();
  }
}
