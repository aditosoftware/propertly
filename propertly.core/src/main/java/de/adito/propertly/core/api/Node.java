package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.common.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
class Node extends AbstractNode
{

  private Object value;
  @Nullable
  private NodeChildren children;


  protected Node(@Nonnull Hierarchy pHierarchy, @Nullable AbstractNode pParent, @Nonnull IPropertyDescription pPropertyDescription)
  {
    super(pHierarchy, pParent, pPropertyDescription);
  }

  @Override
  public Object setValue(Object pValue)
  {
    if ((value == pValue) || (value != null && value.equals(pValue)))
      return value; // nothing changes with equal values.

    if (pValue != null)
    {
      Class type = getProperty().getType();
      //noinspection unchecked
      if (!type.isAssignableFrom(pValue.getClass()))
        throw new IllegalArgumentException("'" + pValue + "' can't be set for field with type '" + type + "'.");
    }
    Object oldValue = value;
    IPropertyPitProvider pppProvider = null;
    if (pValue instanceof IPropertyPitProvider)
      pppProvider = (IPropertyPitProvider) pValue;
    if (pppProvider != null && HierarchyHelper.hasNode(pppProvider) &&
        getHierarchy().equals(HierarchyHelper.getNode(pppProvider).getHierarchy()))
      throw new IllegalStateException("can't set PPP from my own hierarchy.");
    if (oldValue instanceof IPropertyPitProvider)
    {
      if (children != null)
      {
        for (INode child : children.asList())
          child.setValue(null);
      }
      HierarchyHelper.setNode((IPropertyPitProvider) oldValue, null);
    }
    if (pppProvider != null)
    {
      IPropertyPitProvider pppCopy = PropertlyUtility.create(pppProvider);
      value = pppCopy;
      HierarchyHelper.setNode(pppCopy, this);

      if (HierarchyHelper.hasNode(pppProvider))
      {
        INode node = HierarchyHelper.getNode(pppProvider);
        List<INode> childNodes = node.getChildren();
        assert childNodes != null;
        if (children == null)
          children = new NodeChildren();
        else
          children.clear();
        for (INode remoteChild : childNodes)
        {
          IPropertyDescription description = remoteChild.getProperty().getDescription();
          INode newChild = createChild(description);
          children.add(newChild);
          newChild.setValue(remoteChild.getValue());
        }
      }
      else
      {
        Set<IPropertyDescription> descriptions = PPPIntrospector.get(pppProvider.getClass());
        if (children == null)
          children = new NodeChildren();
        else
          children.clear();
        for (IPropertyDescription description : descriptions)
          children.add(createChild(description));
      }
    }
    else
    {
      value = pValue;
      children = null;
    }
    fireValueChange(oldValue, pValue);
    return value;
  }

  @Override
  public Object getValue()
  {
    return value;
  }

  @Override
  public boolean canRead()
  {
    return true;
  }

  @Override
  public boolean canWrite()
  {
    return true;
  }

  @Nullable
  @Override
  public List<INode> getChildren()
  {
    return children == null ? null : children.asList();
  }

  @Nullable
  @Override
  public INode findNode(@Nonnull IPropertyDescription pPropertyDescription)
  {
    return children == null ? null : children.find(pPropertyDescription);
  }

  protected INode createChild(IPropertyDescription pPropertyDescription)
  {
    return new Node(getHierarchy(), this, PropertyDescription.create(pPropertyDescription));
  }

  @Override
  public void addProperty(@Nonnull IPropertyDescription pPropertyDescription)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = findNode(pPropertyDescription);
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);
    if (children == null)
      children = new NodeChildren();
    INode child = createChild(pPropertyDescription);
    children.add(child);
    fireNodeAdded(child.getProperty().getDescription());
  }

  @Override
  public boolean removeProperty(@Nonnull IPropertyDescription pPropertyDescription)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode childNode = findNode(pPropertyDescription);
    if (childNode != null)
    {
      IProperty property = childNode.getProperty();
      if (!(property instanceof DynamicHierarchyProperty))
        throw new IllegalStateException("can't remove: " + getProperty());
      IPropertyDescription description = property.getDescription();
      fireNodeWillBeRemoved(description);
      assert children != null;
      children.remove(childNode);
      fireNodeRemoved(description);
      return true;
    }
    return false;
  }

  @Override
  public void addProperty(int pIndex, @Nonnull IPropertyDescription pPropertyDescription)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = findNode(pPropertyDescription);
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);
    if (children == null)
      children = new NodeChildren();
    INode child = createChild(pPropertyDescription);
    children.add(pIndex, child);
    fireNodeAdded(child.getProperty().getDescription());
  }

  @Override
  public void removeProperty(int pIndex)
  {
    if (!(value instanceof IMutablePropertyPitProvider) || children == null)
      throw new IllegalStateException("not mutable: " + getProperty());
    IProperty property = children.get(pIndex).getProperty();
    if (!(property instanceof DynamicHierarchyProperty))
      throw new IllegalStateException("can't remove: " + getProperty());
    IPropertyDescription description = property.getDescription();
    fireNodeWillBeRemoved(description);
    children.remove(pIndex);
    fireNodeRemoved(description);
  }

  @Override
  public void reorder(Comparator pComparator)
  {
    if (children != null)
      children.reorder(pComparator);
  }

  @Override
  public void rename(String pName)
  {
    IProperty property = getProperty();
    if (!(property instanceof DynamicHierarchyProperty))
      throw new IllegalStateException("can't rename: " + property);
    Node parent = (Node) getParent();
    if (parent != null)
    {
      assert parent.children != null;
      parent.children.rename(property.getDescription(), pName);
    }
    ((PropertyDescription) property.getDescription()).setName(pName);
  }

}
