package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;

import java.util.*;

/**
 * @author j.boesl, 09.11.14
 */
class NodeChildren<T extends INode> implements Iterable<T>
{

  private Map<String, T> childrenMap;
  private List<T> childrenList;

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

  public void add(T pNode)
  {
    add(null, pNode);
  }

  public void add(Integer pIndex, T pNode)
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

  public boolean remove(T pNode)
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
    T node = childrenMap.remove(pPropertyDescription.getName());
    assert node != null;
    childrenMap.put(pName, node);
  }

  public int indexOf(String pName)
  {
    T node = find(pName);
    return childrenList.indexOf(node);
  }

  public void reorder(final Comparator pComparator)
  {
    Collections.sort(childrenList, (o1, o2) -> {
      //noinspection unchecked
      return pComparator.compare(o1.getProperty(), o2.getProperty());
    });
  }

  public List<T> asList()
  {
    return Collections.unmodifiableList(childrenList);
  }

  public T find(String pName)
  {
    return childrenMap.get(pName);
  }

  public T find(IPropertyDescription<?, ?> pPropertyDescription)
  {
    T node = childrenMap.get(pPropertyDescription.getName());
    if (node == null)
      return null;
    boolean fittingTypeAndSourceType = pPropertyDescription.getType().isAssignableFrom(node.getProperty().getType()) &&
        pPropertyDescription.getSourceType().isAssignableFrom(node.getProperty().getDescription().getSourceType());
    return fittingTypeAndSourceType ? node : null;
  }

  public T get(int pIndex)
  {
    return childrenList.get(pIndex);
  }

  @Override
  public Iterator<T> iterator()
  {
    return asList().iterator();
  }
}
