package de.adito.propertly.diff.data;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.diff.common.IValueCompare;

import java.security.MessageDigest;
import java.util.*;

/**
 * @author j.boesl, 16.07.15
 */
public class PropertyNode extends AbstractStructuredPropertlyNode<IProperty, PropertyNode>
{

  private IProperty<?, ?> property;
  private IValueCompare valueCompare;
  private String name;

  public PropertyNode(IProperty<?, ?> pProperty, Map<String, Integer> pHashIdMapping, IValueCompare pValueCompare)
  {
    super(null);
    property = pProperty;
    valueCompare = pValueCompare;
    name = pProperty.getName();
    update(pHashIdMapping);
  }

  private PropertyNode(Integer pId, IProperty<?, ?> pProperty, IValueCompare pValueCompare)
  {
    super(pId);
    property = pProperty;
    valueCompare = pValueCompare;
    name = pProperty.getName();
  }

  public PropertyNode(IProperty<?, ?> pProperty)
  {
    super(-1);
    property = pProperty;
    name = pProperty.getName();
    Object value = pProperty.getValue();
    if (value instanceof IPropertyPitProvider)
    {
      List<IProperty> properties = ((IPropertyPitProvider) value).getPit().getProperties();
      for (IProperty node : properties)
        addChild(new PropertyNode(node));
    }
  }

  @Override
  public String getName()
  {
    return name;
  }

  public void setRef(IProperty<?, ?> pNewRef)
  {
    property = pNewRef;
  }

  @Override
  public IProperty<?, ?> getRef()
  {
    return property;
  }

  public void rename(String pName)
  {
    super.rename(pName);
    name = pName;
  }

  @Override
  protected boolean updateMessageDigest(MessageDigest pMessageDigest)
  {
    Object value = property.getValue();
    if (value == null)
    {
      if (property.isDynamic())
        pMessageDigest.update(UUID.randomUUID().toString().getBytes());
      else
        return false;
    }
    else
    {
      pMessageDigest.update(value.getClass().getCanonicalName().getBytes());
      if (value instanceof IPropertyPitProvider)
        pMessageDigest.update((byte) 0);
      else
        pMessageDigest.update(valueCompare.createEqualityHash(value));
    }
    return true;
  }

  @Override
  public int getRating(PropertyNode pMatch)
  {
    int rate = 0;

    if (property.getName().equals(pMatch.getName()))
      rate += 2;

    IPropertlyNode parent = getParent();
    IPropertlyNode matchParent = pMatch.getParent();
    if (parent == null && matchParent == null)
      rate += 4;
    else if (parent != null && matchParent != null && parent.getId() == matchParent.getId())
      rate += 2;

    if (getRef().getValue() instanceof IPropertyPitProvider)
      return _rateNode(pMatch, rate);
    else
      return _rateValue(pMatch, rate);
  }

  private int _rateNode(PropertyNode pMatch, int pRate)
  {
    List<PropertyNode> children = getChildren();
    List<PropertyNode> matchChildren = pMatch.getChildren();
    if (children.isEmpty() && matchChildren.isEmpty())
      pRate += 2;

    Map<Integer, List<IPropertlyNode>> idMatchChildMap = new HashMap<Integer, List<IPropertlyNode>>();
    for (IPropertlyNode matchChild : matchChildren)
    {
      List<IPropertlyNode> list = idMatchChildMap.get(matchChild.getId());
      if (list == null)
      {
        list = new LinkedList<IPropertlyNode>();
        idMatchChildMap.put(matchChild.getId(), list);
      }
      list.add(matchChild);
    }
    for (Iterator<PropertyNode> childrenItar = children.iterator(); childrenItar.hasNext(); )
    {
      IPropertlyNode child = childrenItar.next();
      List<IPropertlyNode> matches = idMatchChildMap.get(child.getId());
      if (matches != null)
      {
        for (Iterator<IPropertlyNode> matchesItar = matches.iterator(); matchesItar.hasNext(); )
        {
          IPropertlyNode matchChild = matchesItar.next();
          if (child.getName().equals(matchChild.getName()))
          {
            pRate += 1;
            matchesItar.remove();
            childrenItar.remove();
            break;
          }
        }
      }
    }
    return pRate;
  }

  private int _rateValue(PropertyNode pMatch, int pRate)
  {
    return pRate;
  }

  @Override
  public PropertyNode createCopy()
  {
    return new PropertyNode(getId(), getRef(), valueCompare);
  }
}
