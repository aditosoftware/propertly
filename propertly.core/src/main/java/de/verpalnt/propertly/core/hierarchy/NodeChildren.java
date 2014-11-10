package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IPropertyDescription;

import java.util.*;

/**
 * @author j.boesl, 09.11.14
 */
public class NodeChildren
{

  private Map<NodeChildKey, INode> childrenMap;
  private List<INode> childrenList;

  public NodeChildren()
  {
    childrenMap = new HashMap<NodeChildKey, INode>();
    childrenList = new ArrayList<INode>();
  }

  public void clear()
  {
    childrenMap.clear();
    childrenList.clear();
  }

  public void add(INode pNode)
  {
    INode existingNode = childrenMap.put(new NodeChildKey(pNode.getProperty().getDescription()), pNode);
    if (existingNode == null)
      childrenList.add(pNode);
  }

  public void add(int pIndex, INode pNode)
  {
    NodeChildKey nodeChildKey = new NodeChildKey(pNode.getProperty().getDescription());
    if (!childrenMap.containsKey(nodeChildKey))
    {
      childrenList.add(pIndex, pNode);
      childrenMap.put(nodeChildKey, pNode);
    }
  }

  public boolean remove(INode pNode)
  {
    boolean wasRemoved = childrenMap.remove(new NodeChildKey(pNode.getProperty().getDescription())) != null;
    if (wasRemoved)
      childrenList.remove(pNode);
    return wasRemoved;
  }

  public void remove(int pIndex)
  {
    INode removedNode = childrenList.remove(pIndex);
    childrenMap.remove(new NodeChildKey(removedNode.getProperty().getDescription()));
  }

  public List<INode> asList()
  {
    return Collections.unmodifiableList(childrenList);
  }

  public INode find(IPropertyDescription pPropertyDescription)
  {
    return childrenMap.get(new NodeChildKey(pPropertyDescription));
  }

  public INode get(int pIndex)
  {
    return childrenList.get(pIndex);
  }

}
