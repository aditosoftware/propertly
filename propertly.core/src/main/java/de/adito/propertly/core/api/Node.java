package de.adito.propertly.core.api;

import de.adito.propertly.core.common.*;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
class Node extends AbstractNode
{

  @Nullable
  private Object value;
  @Nullable
  private NodeChildren children;


  protected Node(@Nonnull Hierarchy pHierarchy, @Nullable AbstractNode pParent, @Nonnull IPropertyDescription pPropertyDescription)
  {
    super(pHierarchy, pParent, pPropertyDescription);
  }

  @Override
  public Object setValue(Object pValue, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();

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
    if (pppProvider != null && pppProvider.getPit().isValid() &&
        getHierarchy().equals(HierarchyHelper.getNode(pppProvider).getHierarchy()))
      throw new IllegalStateException("can't set PPP from my own hierarchy.");
    if (oldValue instanceof IPropertyPitProvider)
    {
      if (children != null)
      {
        for (INode child : children)
          child.remove();
      }
      HierarchyHelper.setNode((IPropertyPitProvider) oldValue, null);
    }
    if (pppProvider != null)
    {
      IPropertyPitProvider pppCopy = PropertlyUtility.create(pppProvider);
      value = pppCopy;
      HierarchyHelper.setNode(pppCopy, this);

      if (pppProvider.getPit().isValid())
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
          newChild.setValue(remoteChild.getValue(), pAttributes);
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
    fireValueChange(oldValue, value, pAttributes);
    return value;
  }

  @Nullable
  @Override
  public Object getValue()
  {
    return value;
  }

  @Override
  public boolean canRead()
  {
    return isValid();
  }

  @Override
  public boolean canWrite()
  {
    return isValid();
  }

  @Override
  public void remove()
  {
    if (isValid() && children != null)
    {
      for (INode child : children)
        child.remove();
      children = null;
      value = null;
    }
    super.remove();
  }

  @Nullable
  @Override
  public List<INode> getChildren()
  {
    return children == null ? null : children.asList();
  }

  @Nullable
  @Override
  public INode findNode(@Nonnull String pName)
  {
    return children == null ? null : children.find(pName);
  }

  @Nullable
  @Override
  public INode findNode(@Nonnull IPropertyDescription pPropertyDescription)
  {
    return children == null ? null : children.find(pPropertyDescription);
  }

  protected INode createChild(IPropertyDescription pPropertyDescription)
  {
    ensureValid();
    return new Node(getHierarchy(), this, PropertyDescription.create(pPropertyDescription));
  }

  @Override
  public void addProperty(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = findNode(pPropertyDescription);
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);
    if (children == null)
      children = new NodeChildren();
    INode child = createChild(pPropertyDescription);
    children.add(child);
    fireNodeAdded(child.getProperty().getDescription(), pAttributes);
  }

  @Override
  public boolean removeProperty(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode childNode = findNode(pPropertyDescription);
    if (childNode != null)
    {
      IProperty property = childNode.getProperty();
      if (!property.isDynamic())
        throw new IllegalStateException("can't remove: " + getProperty());
      IPropertyDescription description = property.getDescription();
      fireNodeWillBeRemoved(description, pAttributes);
      assert children != null;
      children.remove(childNode);
      fireNodeRemoved(description, pAttributes);
      return true;
    }
    return false;
  }

  @Override
  public void addProperty(int pIndex, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = findNode(pPropertyDescription);
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);
    if (children == null)
      children = new NodeChildren();
    INode child = createChild(pPropertyDescription);
    children.add(pIndex, child);
    fireNodeAdded(child.getProperty().getDescription(), pAttributes);
  }

  @Override
  public void removeProperty(int pIndex, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(value instanceof IMutablePropertyPitProvider) || children == null)
      throw new IllegalStateException("not mutable: " + getProperty());
    IProperty property = children.get(pIndex).getProperty();
    if (!property.isDynamic())
      throw new IllegalStateException("can't remove: " + getProperty());
    IPropertyDescription description = property.getDescription();
    fireNodeWillBeRemoved(description, pAttributes);
    children.remove(pIndex);
    fireNodeRemoved(description, pAttributes);
  }

  @Override
  public int indexOf(@Nonnull IPropertyDescription pPropertyDescription)
  {
    return children == null ? -1 : children.indexOf(pPropertyDescription);
  }

  @Override
  public void reorder(@Nonnull Comparator pComparator, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (children != null)
    {
      children.reorder(pComparator);
      firePropertyOrderChanged(pAttributes);
    }
  }

  @Override
  public void rename(@Nonnull String pName, @Nonnull Set<Object> pAttributes) throws PropertlyRenameException
  {
    ensureValid();
    IProperty property = getProperty();
    if (!property.isDynamic())
      throw new PropertlyRenameException(property, pName);

    try
    {
      String oldName = property.getName();
      Node parent = (Node) getParent();
      if (parent != null)
      {
        assert parent.children != null;
        parent.children.rename(property.getDescription(), pName);
      }
      ((PropertyDescription) property.getDescription()).setName(pName);
      firePropertyNameChanged(oldName, pName, pAttributes);
    }
    catch (Exception e)
    {
      throw new PropertlyRenameException(e, property, pName);
    }
  }

}
