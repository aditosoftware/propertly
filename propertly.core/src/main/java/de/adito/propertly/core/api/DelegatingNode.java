package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 18:17
 */
public class DelegatingNode extends AbstractNode
{
  private INode delegate;
  private IPropertyPitProvider pitProvider;
  @Nullable
  private NodeChildren children;


  public DelegatingNode(@Nonnull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent,
                        @Nonnull INode pDelegate)
  {
    super(pHierarchy, pParent, pDelegate.getProperty().getDescription());
    delegate = pDelegate;
    _alignToDelegate();
  }

  private void _alignToDelegate()
  {
    Object value = delegate.getValue();
    if (value instanceof IPropertyPitProvider)
    {
      IPropertyPitProvider ppp = (IPropertyPitProvider) value;
      ppp = PropertlyUtility.create(ppp);
      HierarchyHelper.setNode(ppp, this);
      pitProvider = ppp;
    }
    else
      pitProvider = null;

    if (children != null)
    {
      for (INode child : children)
        child.remove();
    }

    List<INode> delegateChildren = delegate.getChildren();
    if (delegateChildren != null)
    {
      children = new NodeChildren();
      for (INode node : delegateChildren)
      {
        DelegatingNode child = createChild(node);
        children.add(child);
      }
    }
    else
      children = null;
  }

  protected DelegatingNode createChild(INode pDelegate)
  {
    return new DelegatingNode(getHierarchy(), this, pDelegate);
  }

  @Nonnull
  @Override
  public final DelegatingHierarchy getHierarchy()
  {
    return (DelegatingHierarchy) super.getHierarchy();
  }

  @Override
  @Nullable
  public Object setValue(@Nullable Object pValue, @Nonnull Set<Object> pAttributes)
  {
    Object oldValue = getValue();
    delegate.setValue(pValue, pAttributes);
    _alignToDelegate();
    Object newValue = getValue();
    fireValueChange(oldValue, newValue, pAttributes);
    return newValue;
  }

  @Override
  public Object getValue()
  {
    return pitProvider == null && delegate != null ? delegate.getValue() : pitProvider;
  }

  @Override
  public boolean canRead()
  {
    return delegate.canRead();
  }

  @Override
  public boolean canWrite()
  {
    return delegate.canWrite();
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

  @Override
  public INode addProperty(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(pitProvider instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = findNode(pPropertyDescription);
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);

    INode delegateChild = delegate.addProperty(pPropertyDescription, pAttributes);
    DelegatingNode child = createChild(delegateChild);
    if (children == null)
      children = new NodeChildren();
    children.add(child);
    fireNodeAdded(child.getProperty().getDescription(), pAttributes);
    return child;
  }

  @Override
  public boolean removeProperty(@Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(pitProvider instanceof IMutablePropertyPitProvider))
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
  public INode addProperty(int pIndex, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(pitProvider instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = findNode(pPropertyDescription);
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);

    INode delegateChild = delegate.addProperty(pIndex, pPropertyDescription, pAttributes);
    DelegatingNode child = createChild(delegateChild);
    if (children == null)
      children = new NodeChildren();
    children.add(pIndex, child);
    fireNodeAdded(child.getProperty().getDescription(), pAttributes);
    return child;
  }

  @Override
  public void removeProperty(int pIndex, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(pitProvider instanceof IMutablePropertyPitProvider) || children == null)
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
      delegate.reorder(pComparator, pAttributes);
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

    delegate.rename(pName, pAttributes);
    try
    {
      String oldName = property.getName();
      DelegatingNode parent = (DelegatingNode) getParent();
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

  @Override
  public boolean isValid()
  {
    return delegate != null && delegate.isValid();
  }

  @Override
  public void remove()
  {
    delegate.remove();
    if (isValid() && children != null)
    {
      for (INode child : children)
        child.remove();
    }
    delegate.remove();
    children = null;
    pitProvider = null;
    delegate = null;
    super.remove();
  }
}
