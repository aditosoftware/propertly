package de.adito.propertly.core.api;

import de.adito.propertly.core.common.ListenerList;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.IMutablePropertyPitProvider;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  private NodeChildren<AbstractNode> children;


  protected DelegatingNode(@Nonnull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent,
                           @Nonnull INode pDelegate)
  {
    this(pHierarchy, pParent, pDelegate.getProperty().getDescription(), pDelegate);
  }

  protected DelegatingNode(@Nonnull DelegatingHierarchy pHierarchy, @Nullable AbstractNode pParent,
                           @Nonnull IPropertyDescription pPropertyDescription,
                           @Nonnull INode pDelegate)
  {
    super(pHierarchy, pParent, pPropertyDescription);
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
      for (AbstractNode child : children)
        child.remove(Collections.emptySet());
    }

    List<? extends INode> delegateChildren = delegate.getChildrenStream()
        .map(stream -> stream.collect(Collectors.toList()))
        .orElse(Collections.emptyList());
    if (delegateChildren != null)
    {
      children = new NodeChildren<>();
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
  protected void updateValue(@Nonnull IPropertyPitProvider pValue, @Nonnull Set<Object> pAttributes)
  {
    delegate.setValue(pValue, pAttributes);
    _alignToDelegate();
  }

  @Override
  protected void updateValue(@Nullable Object pValue, @Nonnull Set<Object> pAttributes)
  {
    delegate.setValue(pValue, pAttributes);
    _alignToDelegate();
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

  @Nonnull
  @Override
  public Optional<? extends Stream<AbstractNode>> getChildrenStream()
  {
    return children == null ? Optional.empty() : Optional.of(children.asList().stream());
  }

  @Nullable
  @Override
  public INode findNode(@Nonnull String pName)
  {
    return children == null ? null : children.find(pName);
  }

  @Nonnull
  @Override
  public INode getNode(int pIndex) throws IndexOutOfBoundsException
  {
    if (children == null)
      throw new IndexOutOfBoundsException();
    return children.get(pIndex);
  }

  @Override
  public INode addProperty(@Nullable Integer pIndex, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
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
      children = new NodeChildren<>();
    children.add(pIndex, child);
    firePropertyAdded(child.getProperty().getDescription(), pAttributes);
    return child;
  }

  @Override
  protected void removeChild(AbstractNode pNode)
  {
    if (children != null)
      children.remove(pNode);
  }

  @Override
  protected void invalidate(@Nonnull Set<Object> pAttributes)
  {
    delegate.remove(pAttributes);
    children = null;
    pitProvider = null;
    delegate = null;
    super.invalidate(pAttributes);
  }

  @Override
  public int indexOf(@Nonnull String pName)
  {
    return children == null ? -1 : children.indexOf(pName);
  }

  @Override
  public void reorder(@Nonnull Comparator pComparator, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (children != null)
    {
      ListenerList<Runnable> onFinish = new ListenerList<>();
      firePropertyOrderWillBeChanged(onFinish::addStrongListener, pAttributes);
      delegate.reorder(pComparator, pAttributes);
      children.reorder(pComparator);
      firePropertyOrderChanged(pAttributes);
      onFinish.forEach(Runnable::run);
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
      DelegatingNode parent = (DelegatingNode) getParent();
      if (parent != null)
      {
        assert parent.children != null;
        parent.children.rename(property.getDescription(), pName);
      }
      delegate.rename(pName, pAttributes);
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

}
