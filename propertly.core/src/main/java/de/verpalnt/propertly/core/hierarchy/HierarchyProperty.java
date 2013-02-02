package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.IProperty;
import de.verpalnt.propertly.core.IPropertyDescription;

/**
 * @author PaL
 *         Date: 30.01.13
 *         Time: 00:30
 */
public class HierarchyProperty implements IProperty
{

  private Node node;
  private IPropertyDescription propertyDescription;

  public HierarchyProperty(Node pNode, IPropertyDescription pPropertyDescription)
  {
    node = pNode;
    propertyDescription = pPropertyDescription;
  }

  @Override
  public IPropertyDescription getDescription()
  {
    return propertyDescription;
  }

  @Override
  public Object getValue()
  {
    return node.getValue();
  }

  @Override
  public Object setValue(Object pValue)
  {
    return node.setValue(pValue);
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "{" + propertyDescription + ", value=" + getValue() + '}';
  }

}
