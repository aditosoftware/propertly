package de.adito.propertly.core.api;

import de.adito.propertly.core.common.ListenerList;
import de.adito.propertly.core.common.PPPIntrospector;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.common.exception.PropertlyRenameException;
import de.adito.propertly.core.spi.IMutablePropertyPitProvider;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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
  private NodeChildren<AbstractNode> children;


  public Node(@Nonnull Hierarchy pHierarchy, @Nullable AbstractNode pParent, @Nonnull IPropertyDescription pPropertyDescription)
  {
    super(pHierarchy, pParent, pPropertyDescription);
  }

  @Override
  protected void updateValue(@Nonnull IPropertyPitProvider pValue, @Nonnull Set<Object> pAttributes)
  {
    IPropertyPitProvider pppCopy = PropertlyUtility.create(pValue);
    value = pppCopy;
    HierarchyHelper.setNode(pppCopy, this);

    if (pValue.getPit().isValid())
    {
      INode node = HierarchyHelper.getNode(pValue);
      if (children == null)
        children = new NodeChildren<>();
      node.getChildrenStream().ifPresent(stream -> {
        stream.forEach(remoteChild -> {
          IPropertyDescription description = remoteChild.getProperty().getDescription();
          AbstractNode newChild = createChild(description);
          children.add(newChild);
          newChild.setValue(remoteChild.getValue(), pAttributes);
        });
      });
    }
    else
    {
      Set<IPropertyDescription> descriptions = PPPIntrospector.get(pValue.getClass());
      if (children == null)
        children = new NodeChildren<>();
      for (IPropertyDescription description : descriptions)
        children.add(createChild(description));
    }
  }

  @Override
  protected void updateValue(@Nullable Object pValue, @Nonnull Set<Object> pAttributes)
  {
    value = pValue;
    children = null;
  }

  @Nullable
  @Override
  public Object getValue()
  {
    return value;
  }

  @Override
  protected void removeChild(AbstractNode pNode)
  {
    assert children != null;
    children.remove(pNode);
  }

  @Override
  protected void invalidate(@Nonnull Set<Object> pAttributes)
  {
    super.invalidate(pAttributes);
    children = null;
    value = null;
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

  protected AbstractNode createChild(IPropertyDescription pPropertyDescription)
  {
    ensureValid();
    return new Node(getHierarchy(), this, pPropertyDescription);
  }

  @Override
  public INode addProperty(@Nullable Integer pIndex, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set<Object> pAttributes)
  {
    ensureValid();
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = findNode(pPropertyDescription.getName());
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);
    if (children == null)
      children = new NodeChildren<>();
    AbstractNode child = createChild(pPropertyDescription);
    children.add(pIndex, child);
    firePropertyAdded(child.getProperty().getDescription(), pAttributes);
    return child;
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
