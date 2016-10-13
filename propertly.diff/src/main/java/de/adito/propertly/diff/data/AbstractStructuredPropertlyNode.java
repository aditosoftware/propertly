package de.adito.propertly.diff.data;

import de.adito.propertly.core.common.path.*;

import java.util.*;

/**
 * @author j.boesl, 16.07.15
 */
public abstract class AbstractStructuredPropertlyNode<T, S extends AbstractStructuredPropertlyNode<T, S>> extends AbstractPropertlyNode<T, S>
{

  private S parent;
  private Map<String, S> children = new HashMap<String, S>();
  private List<S> orderedChildren = new LinkedList<S>();

  public AbstractStructuredPropertlyNode(Integer pId)
  {
    super(pId);
  }

  void setParent(S pParent)
  {
    if (parent != null && !parent.equals(pParent))
      parent.removeChild((S) this);
    parent = pParent;
  }

  public void addChild(S pNode)
  {
    addChild(-1, pNode, null);
  }

  @Override
  public void addChild(int pIndex, S pNode, String pNewName)
  {
    if (pNewName != null && !pNode.getName().equals(pNewName))
    {
      pNode.remove();
      pNode.rename(pNewName);
    }

    S oldNode = children.get(pNode.getName());
    if (oldNode != null)
      oldNode.remove();

    pNode.setParent((S) this);

    children.put(pNode.getName(), pNode);
    if (pIndex >= 0)
      orderedChildren.add(pIndex, pNode);
    else
      orderedChildren.add(pNode);
  }

  @Override
  public void remove()
  {
    setParent(null);
  }

  void removeChild(S pNode)
  {
    S removedNode = children.remove(pNode.getName());
    assert pNode.equals(removedNode);
    orderedChildren.remove(pNode);
  }

  public void rename(String pName)
  {
    AbstractStructuredPropertlyNode<T, S> p = getParent();
    if (p != null)
    {
      AbstractStructuredPropertlyNode<T, S> oldNode = p.getChild(pName);
      if (oldNode != null)
        oldNode.remove();

      p.children.remove(getName());
      p.children.put(pName, (S) this);
    }
  }

  @Override
  public S getParent()
  {
    return parent;
  }

  @Override
  public List<S> getChildren()
  {
    return new LinkedList<S>(orderedChildren);
  }

  @Override
  public S getChild(String pName)
  {
    return children.get(pName);
  }

  @Override
  public IPropertyPath getPath()
  {
    List<String> pathElements = new LinkedList<String>();
    S p = (S) this;
    while (p != null)
    {
      pathElements.add(0, p.getName());
      p = p.getParent();
    }
    return new PropertyPath(pathElements);
  }

  @Override
  public S resolveChild(IPropertyPath pPath)
  {
    S n = null;
    for (String element : pPath.getPathElements())
    {
      if (n == null)
        n = (S) this;
      else
        n = n.getChild(element);

      if (n == null || !n.getName().equals(element))
        throw new RuntimeException("can't resolve child from '" + pPath + "' @ '" + element + "' for '" + this + "'.");
    }
    return n;
  }
}
