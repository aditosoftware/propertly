package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.IMutablePropertyPitProvider;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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


  protected DelegatingNode(@NotNull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent,
                           @NotNull INode pDelegate)
  {
    this(pHierarchy, pParent, pDelegate.getProperty().getDescription(), pDelegate.getProperty().isDynamic(), pDelegate);
  }

  protected DelegatingNode(@NotNull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent,
                           @NotNull IPropertyDescription pPropertyDescription, boolean pDynamic,
                           @NotNull INode pDelegate)
  {
    super(pHierarchy, pParent, pPropertyDescription, pDynamic);
    delegate = pDelegate;
    _alignToDelegate();
  }

  private void _alignToDelegate()
  {
    Object value = delegate.getValue();
    if (value instanceof IPropertyPitProvider && pitProvider == null)
    {
      IPropertyPitProvider ppp = (IPropertyPitProvider) value;
      ppp = PropertlyUtility.create(ppp);
      HierarchyHelper.setNode(ppp, this);
      pitProvider = ppp;
    }
    else if (value == null)
      pitProvider = null;

    List<INode> delegateChildren = delegate.getChildren();
    if (delegateChildren != null)
    {
      // create children
      if (children == null)
        children = new NodeChildren();

      Set<INode> childrenToRemove = new HashSet<>(children.asList());

      // update children
      for (INode delegateChild : delegateChildren)
      {
        IPropertyDescription<?, ?> delegateDescription = delegateChild.getProperty().getDescription();
        DelegatingNode myChild = (DelegatingNode) children.find(delegateDescription);
        if (myChild == null)
          children.add(createChild(delegateChild));
        childrenToRemove.removeIf(pNode -> pNode.getProperty().getDescription().equals(delegateDescription));
      }

      // remove children
      for (INode nodeToRemove : childrenToRemove)
        children.remove(nodeToRemove);
    }
    else
    {
      // invalidate, because delegate does not have children (anymore)
      if (children != null)
        for (INode child : children)
          child.remove();
      children = null;
    }
  }

  protected DelegatingNode createChild(INode pDelegate)
  {
    return new DelegatingNode(getHierarchy(), this, pDelegate);
  }

  @NotNull
  @Override
  public final DelegatingHierarchy getHierarchy()
  {
    return (DelegatingHierarchy) super.getHierarchy();
  }

  @Override
  @Nullable
  public Object setValue(@Nullable Object pValue, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    Object oldValue = getValueInternal();
    List<Runnable> onFinish = new ArrayList<>();
    fireValueWillBeChange(oldValue, pValue, onFinish::add, pAttributes);
    delegate.setValue(pValue, pAttributes);
    clearListeners();
    _alignToDelegate();
    Object newValue = getValueInternal();
    fireValueChange(oldValue, newValue, pAttributes);
    onFinish.forEach(Runnable::run);
    return newValue;
  }

  @Override
  public Object getValue()
  {
    return getValueInternal();
  }

  @Override
  public boolean canRead()
  {
    return isValid() && delegate.canRead();
  }

  @Override
  public boolean canWrite()
  {
    return isValid() && delegate.canWrite();
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

  @Override
  public INode addProperty(@Nullable Integer pIndex, @NotNull IPropertyDescription pPropertyDescription, @NotNull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(pitProvider instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = findNode(pPropertyDescription.getName());
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
  public boolean removeProperty(@NotNull IPropertyDescription pPropertyDescription, @NotNull Set<Object> pAttributes)
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
      List<Runnable> onFinish = new ArrayList<>();
      fireNodeWillBeRemoved(description, onFinish::add, pAttributes);
      assert children != null;
      delegate.removeProperty(pPropertyDescription, pAttributes);
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
    if (!(pitProvider instanceof IMutablePropertyPitProvider) || children == null)
      throw new IllegalStateException("not mutable: " + getProperty());
    IProperty property = children.get(pIndex).getProperty();
    if (!property.isDynamic())
      throw new IllegalStateException("can't remove: " + getProperty());
    IPropertyDescription description = property.getDescription();
    List<Runnable> onFinish = new ArrayList<>();
    fireNodeWillBeRemoved(description, onFinish::add, pAttributes);
    delegate.removeProperty(pIndex, pAttributes);
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
    if (children != null)
    {
      List<Runnable> onFinish = new ArrayList<>();
      firePropertyOrderWillBeChanged(onFinish::add, pAttributes);
      children.reorder(pComparator);
      delegate.reorder(Comparator.<IProperty>comparingInt(p -> children.indexOf(p.getDescription())), pAttributes);
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

    try
    {
      delegate.rename(pName, pAttributes);

      String oldName = property.getName();
      DelegatingNode parent = (DelegatingNode) getParent();
      if (parent != null)
      {
        assert parent.children != null;
        parent.children.rename(property.getDescription(), pName);
      }
      ((HierarchyProperty) property).setPropertyDescription(property.getDescription().copy(pName));
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
    assert delegate != null;
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

  protected Object getValueInternal()
  {
    return pitProvider == null && delegate != null ? delegate.getValue() : pitProvider;
  }
}
