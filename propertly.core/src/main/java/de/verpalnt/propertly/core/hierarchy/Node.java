package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;
import de.verpalnt.propertly.core.common.PPPIntrospector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Node extends AbstractNode
{

  private Object value;
  private List<INode> children;
  private List<IPropertyEventListener> listeners;


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
    if (pppProvider != null && HierarchyHelper.getNode(pppProvider) != null &&
        getHierarchy().equals(HierarchyHelper.getNode(pppProvider).getHierarchy()))
      throw new IllegalStateException("can't set PPP from my own hierarchy.");
    if (oldValue instanceof IPropertyPitProvider)
    {
      for (INode child : children)
        child.setValue(null);
      HierarchyHelper.setNode((IPropertyPitProvider) oldValue, null);
    }
    if (pppProvider != null)
    {
      IPropertyPitProvider pppCopy;
      try
      {
        pppCopy = pppProvider.getClass().newInstance();
      }
      catch (Exception e)
      {
        throw new RuntimeException("can't instantiate: " + pppProvider);
      }
      value = pppCopy;
      HierarchyHelper.setNode(pppCopy, this);
      INode node = HierarchyHelper.getNode(pppProvider);

      if (node == null)
      {
        Set<IPropertyDescription> descriptions = PPPIntrospector.get(pppProvider.getClass());
        List<INode> nodes = new ArrayList<INode>(descriptions.size());
        for (IPropertyDescription description : descriptions)
          nodes.add(createChild(description));
        children = nodes;
      } else
      {
        List<INode> childNodes = node.getChildren();
        assert childNodes != null;
        List<INode> newNodes = new ArrayList<INode>(childNodes.size());
        for (INode child : childNodes)
          newNodes.add(createChild(child.getProperty().getDescription()));
        children = newNodes;
        for (int i = 0; i < childNodes.size(); i++)
          newNodes.get(i).setValue(childNodes.get(i));
      }
    } else
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
  @Nullable
  public List<INode> getChildren()
  {
    return children;
  }

  protected INode createChild(IPropertyDescription pPropertyDescription)
  {
    return new Node(getHierarchy(), this, pPropertyDescription);
  }

  @Override
  public void addProperty(IPropertyDescription pPropertyDescription)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = find(pPropertyDescription.getName());
    if (node != null)
      throw new IllegalStateException("name already exists: " + pPropertyDescription);
    children.add(createChild(pPropertyDescription));
    fireNodeAdded(pPropertyDescription);
  }

  @Override
  public boolean removeProperty(String pName)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    INode node = find(pName);
    if (node != null)
    {
      IProperty nProp = node.getProperty();
      if (IMutablePropertyPitProvider.class.isAssignableFrom(nProp.getType()))
      {
        IPropertyDescription descr = nProp.getDescription();
        fireNodeWillBeRemoved(descr);
        children.remove(node);
        fireNodeRemoved(descr);
        return true;
      }
    }
    return false;
  }

}
