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
  private List<AbstractNode> children;
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
    if (pppProvider != null && pppProvider.getPit().getNode() != null &&
        getHierarchy().equals(pppProvider.getPit().getNode().getHierarchy()))
      throw new IllegalStateException("can't set PPP from my own hierarchy.");
    if (oldValue instanceof IPropertyPitProvider)
    {
      for (AbstractNode child : children)
        child.setValue(null);
      ((IPropertyPitProvider) oldValue).getPit().setNode(null);
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
      pppCopy.getPit().setNode(this);
      AbstractNode node = pppProvider.getPit().getNode();

      if (node == null)
      {
        Set<IPropertyDescription> descriptions = PPPIntrospector.get(pppProvider.getClass());
        List<AbstractNode> nodes = new ArrayList<AbstractNode>(descriptions.size());
        for (IPropertyDescription description : descriptions)
          nodes.add(createChild(description));
        children = nodes;
      }
      else
      {
        List<AbstractNode> childNodes = node.getChildren();
        assert childNodes != null;
        List<AbstractNode> newNodes = new ArrayList<AbstractNode>(childNodes.size());
        for (AbstractNode child : childNodes)
          newNodes.add(createChild(child.getProperty().getDescription()));
        children = newNodes;
        for (int i = 0; i < childNodes.size(); i++)
          newNodes.get(i).setValue(childNodes.get(i));
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
  @Nullable
  public List<AbstractNode> getChildren()
  {
    return children;
  }

  protected AbstractNode createChild(IPropertyDescription pPropertyDescription)
  {
    return new Node(getHierarchy(), this, pPropertyDescription);
  }

  @Override
  public void addProperty(IPropertyDescription pPropertyDescription)
  {
    if (!(value instanceof IMutablePropertyPitProvider))
      throw new IllegalStateException("not mutable: " + getProperty());
    AbstractNode node = find(pPropertyDescription.getName());
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
    AbstractNode node = find(pName);
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
