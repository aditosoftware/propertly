package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PPPIntrospector;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.common.path.PropertyPath;
import de.adito.propertly.core.spi.IMutablePropertyPitProvider;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Node extends AbstractNode
{

  @Nullable
  private Object value;
  @Nullable
  private NodeChildren children;


  public Node(@NotNull Hierarchy pHierarchy, @Nullable AbstractNode pParent, @NotNull IPropertyDescription pPropertyDescription,
              boolean pDynamic)
  {
    super(pHierarchy, pParent, pPropertyDescription, pDynamic);
  }

  @Override
  public Object setValue(Object pValue, @NotNull Set<Object> pAttributes)
  {
    ensureValid();

    if ((value == pValue) || (value != null && value.equals(pValue)))
      return value; // nothing changes with equal values.

    if (pValue != null) {
      Class type = getProperty().getType();
      //noinspection unchecked
      if (!type.isAssignableFrom(pValue.getClass()))
        throw new IllegalArgumentException("'" + pValue + "' can't be set for field with type '" + type + "'.");
    }
    Object oldValue = value;
    IPropertyPitProvider pppProvider = null;
    if (pValue instanceof IPropertyPitProvider) {
      pppProvider = (IPropertyPitProvider) pValue;

      if (pppProvider.getPit().isValid() && getHierarchy().equals(HierarchyHelper.getNode(pppProvider).getHierarchy())) {
        PropertyPath path = new PropertyPath(getProperty());
        PropertyPath pppPath = new PropertyPath(pppProvider);
        if (path.equals(pppPath))
          return pppProvider;
        if (path.isParentOf(pppPath) || pppPath.isParentOf(path)) {
          String message = MessageFormat.format(
              "The new value for a property mustn't be the parent or a child (set ''{0}'' to ''{1}'').", pppPath, path);
          throw new IllegalStateException(message);
        }
      }
    }

    List<Runnable> onFinish = new ArrayList<>();
    fireValueWillBeChange(oldValue, pValue, onFinish::add, pAttributes);

    if (oldValue instanceof IPropertyPitProvider) {
      if (children != null) {
        for (INode child : children)
          child.remove();
      }
      HierarchyHelper.setNode((IPropertyPitProvider) oldValue, null);
    }
    clearListeners();
    if (pppProvider != null) {
      IPropertyPitProvider pppCopy = PropertlyUtility.create(pppProvider);
      value = pppCopy;
      HierarchyHelper.setNode(pppCopy, this);

      if (pppProvider.getPit().isValid()) {
        INode node = HierarchyHelper.getNode(pppProvider);
        List<INode> childNodes = node.getChildren();
        assert childNodes != null;
        if (children == null)
          children = new NodeChildren();
        else
          children.clear();
        for (INode remoteChild : childNodes) {
          IProperty property = remoteChild.getProperty();
          IPropertyDescription description = property.getDescription();
          INode newChild = createChild(description, property.isDynamic());
          children.add(newChild);
          newChild.setValue(remoteChild.getValue(), pAttributes);
        }
      }
      else {
        Set<IPropertyDescription> descriptions = PPPIntrospector.get(pppProvider.getClass());
        if (children == null)
          children = new NodeChildren();
        else
          children.clear();
        for (IPropertyDescription description : descriptions)
          children.add(createChild(description, false));
      }
    }
    else {
      value = pValue;
      children = null;
    }
    fireValueChange(oldValue, value, pAttributes);
    onFinish.forEach(Runnable::run);
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
    if (isValid() && children != null) {
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
  public INode findNode(@NotNull String pName)
  {
    return children == null ? null : children.find(pName);
  }

  @Nullable
  @Override
  public INode findNode(@NotNull IPropertyDescription pPropertyDescription)
  {
    return children == null ? null : children.find(pPropertyDescription);
  }

  protected INode createChild(IPropertyDescription pPropertyDescription, boolean pIsDynamic)
  {
    ensureValid();
    return new Node(getHierarchy(), this, pPropertyDescription, pIsDynamic);
  }

  @Override
  public INode addProperty(@Nullable Integer pIndex, @NotNull IPropertyDescription pPropertyDescription, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = findNode(pPropertyDescription.getName());
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);
    if (children == null)
      children = new NodeChildren();
    INode child = createChild(pPropertyDescription, true);
    children.add(pIndex, child);
    fireNodeAdded(child.getProperty().getDescription(), pAttributes);
    return child;
  }

  @Override
  public boolean removeProperty(@NotNull IPropertyDescription pPropertyDescription, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode childNode = findNode(pPropertyDescription);
    if (childNode != null) {
      IProperty property = childNode.getProperty();
      if (!property.isDynamic())
        throw new IllegalStateException("can't remove: " + getProperty());
      IPropertyDescription description = property.getDescription();
      List<Runnable> onFinish = new ArrayList<>();
      fireNodeWillBeRemoved(description, onFinish::add, pAttributes);
      assert children != null;
      children.remove(childNode);
      HierarchyHelper.getNode(property).remove();
      fireNodeRemoved(description, pAttributes);
      onFinish.forEach(Runnable::run);
      return true;
    }
    return false;
  }

  @Override
  public void removeProperty(int pIndex, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(value instanceof IMutablePropertyPitProvider) || children == null)
      throw new IllegalStateException("not mutable: " + getProperty());
    IProperty property = children.get(pIndex).getProperty();
    if (!property.isDynamic())
      throw new IllegalStateException("can't remove: " + getProperty());
    IPropertyDescription description = property.getDescription();
    List<Runnable> onFinish = new ArrayList<>();
    fireNodeWillBeRemoved(description, onFinish::add, pAttributes);
    children.remove(pIndex);
    HierarchyHelper.getNode(property).remove();
    fireNodeRemoved(description, pAttributes);
    onFinish.forEach(Runnable::run);
  }

  @Override
  public int indexOf(@NotNull IPropertyDescription pPropertyDescription)
  {
    return children == null ? -1 : children.indexOf(pPropertyDescription);
  }

  @Override
  public void reorder(@NotNull Comparator pComparator, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    if (children != null) {
      List<Runnable> onFinish = new ArrayList<>();
      firePropertyOrderWillBeChanged(onFinish::add, pAttributes);
      children.reorder(pComparator);
      firePropertyOrderChanged(pAttributes);
      onFinish.forEach(Runnable::run);
    }
  }

  @Override
  public void rename(@NotNull String pName, @NotNull Set<Object> pAttributes) throws PropertlyRenameException
  {
    ensureValid();
    IProperty property = getProperty();
    if (!property.isDynamic())
      throw new PropertlyRenameException(property, pName);

    try {
      String oldName = property.getName();
      Node parent = (Node) getParent();
      if (parent != null) {
        assert parent.children != null;
        parent.children.rename(property.getDescription(), pName);
      }
      ((HierarchyProperty) property).setPropertyDescription(property.getDescription().copy(pName));
      firePropertyNameChanged(oldName, pName, pAttributes);
    }
    catch (Exception e) {
      throw new PropertlyRenameException(e, property, pName);
    }
  }

}
