package de.adito.propertly.core.api;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

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
    alignToDelegate();
  }

  protected void alignToDelegate()
  {
    Object value = executeReadOnDelegate(INode::getValue);
    if (value instanceof IPropertyPitProvider && (pitProvider == null || value.getClass() != pitProvider.getClass()))
    {
      IPropertyPitProvider ppp = (IPropertyPitProvider) value;
      ppp = PropertlyUtility.create(ppp);
      HierarchyHelper.setNode(ppp, this);
      pitProvider = ppp;

      // invalidate children, because we have to fully recalculate again
      _invalidateChildren();
    }
    else if (value == null)
      pitProvider = null;

    List<INode> delegateChildren = executeReadOnDelegate(INode::getChildren);
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
      _invalidateChildren();
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

    executeWriteOnDelegate(pDelegate -> {
      pDelegate.setValue(pValue, pAttributes);
      clearListeners();
    });

    alignToDelegate();
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
    return isValid() && executeReadOnDelegate(INode::canRead);
  }

  @Override
  public boolean canWrite()
  {
    return isValid() && executeReadOnDelegate(INode::canWrite);
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
    executeWriteOnDelegate(pDelegate -> pDelegate.addProperty(pIndex, pPropertyDescription, pAttributes));
    DelegatingNode child = createChild(executeReadOnDelegate(pDelegate -> pDelegate.findNode(pPropertyDescription)));
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
      executeWriteOnDelegate(pDelegate -> pDelegate.removeProperty(pPropertyDescription, pAttributes));
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
    executeWriteOnDelegate(pDelegate -> pDelegate.removeProperty(pIndex, pAttributes));
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
      executeWriteOnDelegate(pDelegate -> pDelegate.reorder(Comparator.<IProperty>comparingInt(p -> children.indexOf(p.getDescription())), pAttributes));
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
      executeWriteOnDelegate(pDelegate -> pDelegate.rename(pName, pAttributes));

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
    return executeReadOnDelegate(INode::isValid) == Boolean.TRUE;
  }

  @Override
  public void remove()
  {
    assert delegate != null;
    executeWriteOnDelegate(INode::remove);
    if (isValid() && children != null)
    {
      for (INode child : children)
        child.remove();
    }
    executeWriteOnDelegate(INode::remove);
    children = null;
    pitProvider = null;
    delegate = null;
    super.remove();
  }

  protected void executeWriteOnDelegate(@NotNull Consumer<INode> pOnDelegate)
  {
    pOnDelegate.accept(delegate);
  }

  protected <T> T executeReadOnDelegate(@NotNull Function<INode, T> pOnDelegate)
  {
    if(delegate == null)
      return null;

    return pOnDelegate.apply(delegate);
  }

  protected Object getValueInternal()
  {
    return pitProvider == null && delegate != null ? executeReadOnDelegate(INode::getValue) : pitProvider;
  }

  protected boolean hasCreatedCopyOfValue()
  {
    return pitProvider != null;
  }

  private void _invalidateChildren()
  {
    if (children != null)
      for (INode child : children)
        child.remove();
    children = null;
  }
}
