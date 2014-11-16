package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;

import java.util.*;

/**
 * @author j.boesl, 09.11.14
 */
public class NodeChildren
{

  private Map<String, INode> childrenMap;
  private List<INode> childrenList;

  public NodeChildren()
  {
    childrenMap = new HashMap<String, INode>();
    childrenList = new ArrayList<INode>();
  }

  public void clear()
  {
    childrenMap.clear();
    childrenList.clear();
  }

  public void add(INode pNode)
  {
    INode existingNode = childrenMap.put(pNode.getProperty().getName(), pNode);
    if (existingNode == null)
      childrenList.add(pNode);
  }

  public void add(int pIndex, INode pNode)
  {
    String name = pNode.getProperty().getName();
    if (!childrenMap.containsKey(name))
    {
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

  public void reorder(final Comparator pComparator)
  {
    Collections.sort(childrenList, new Comparator<INode>()
    {
      @Override
      public int compare(INode o1, INode o2)
      {
        return pComparator.compare(o1.getValue(), o2.getValue());
      }
    });
  }

  public List<INode> asList()
  {
    return Collections.unmodifiableList(childrenList);
  }

  public INode find(IPropertyDescription<?, ?> pPropertyDescription)
  {
    INode node = childrenMap.get(pPropertyDescription.getName());
    if (node == null)
      return null;
    return pPropertyDescription.getType().isAssignableFrom(node.getProperty().getType()) ? node : null;
  }

  public INode get(int pIndex)
  {
    return childrenList.get(pIndex);
  }

}
