package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * NodeChildren with map/list storage for dynamic property pits.
 * Static property pits use generated subclasses of AbstractNodeChildren
 * that have no map/list overhead.
 *
 * @author j.boesl, 09.11.14
 */
class NodeChildren extends AbstractNodeChildren
{
  private final Map<String, INode> childrenMap = new HashMap<>();
  private final List<INode> childrenList = new ArrayList<>();

  @Override
  public void clear()
  {
    childrenMap.clear();
    childrenList.clear();
  }

  @Override
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

  @Override
  public boolean remove(@NotNull INode pNode)
  {
    IProperty property = pNode.getProperty();
    boolean wasRemoved = childrenMap.remove(property.getName()) != null;
    if (wasRemoved)
      childrenList.remove(pNode);
    return wasRemoved;
  }

  @Override
  public void remove(int pIndex)
  {
    INode removedNode = childrenList.remove(pIndex);
    childrenMap.remove(removedNode.getProperty().getName());
  }

  @Override
  public void rename(@NotNull IPropertyDescription pPropertyDescription, @NotNull String pName)
  {
    if (childrenMap.containsKey(pName))
      throw new RuntimeException("property with name '" + pName + "' already exists.");
    INode node = childrenMap.remove(pPropertyDescription.getName());
    assert node != null;
    childrenMap.put(pName, node);
  }

  @Override
  public int indexOf(@NotNull IPropertyDescription pPropertyDescription)
  {
    INode node = find(pPropertyDescription);
    return childrenList.indexOf(node);
  }

  @Override
  public void reorder(@NotNull Comparator pComparator)
  {
    childrenList.sort((o1, o2) -> {
      //noinspection unchecked
      return pComparator.compare(o1.getProperty(), o2.getProperty());
    });
  }

  @NotNull
  @Override
  public List<INode> asList()
  {
    return Collections.unmodifiableList(childrenList);
  }

  @Nullable
  @Override
  public INode find(@NotNull String pName)
  {
    return childrenMap.get(pName);
  }

  @Nullable
  @Override
  public INode find(@NotNull IPropertyDescription<?, ?> pPropertyDescription)
  {
    INode node = childrenMap.get(pPropertyDescription.getName());
    if (node == null)
      return null;
    boolean fittingTypeAndSourceType = pPropertyDescription.getType().isAssignableFrom(node.getProperty().getType()) &&
        pPropertyDescription.getSourceType().isAssignableFrom(node.getProperty().getDescription().getSourceType());
    return fittingTypeAndSourceType ? node : null;
  }

  @NotNull
  @Override
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
