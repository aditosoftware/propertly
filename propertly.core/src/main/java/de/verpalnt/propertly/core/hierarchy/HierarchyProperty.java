package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author PaL
 *         Date: 30.01.13
 *         Time: 00:30
 */
class HierarchyProperty implements IProperty
{

  private Node node;
  private IPropertyDescription propertyDescription;

  HierarchyProperty(Node pNode, IPropertyDescription pPropertyDescription)
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
  public Class getParentType()
  {
    return propertyDescription.getParentType();
  }

  @Override
  public Class getType()
  {
    return propertyDescription.getType();
  }

  @Override
  public String getName()
  {
    return propertyDescription.getName();
  }

  @Override
  public List<? extends Annotation> getAnnotations()
  {
    return propertyDescription.getAnnotations();
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "{" + propertyDescription + ", value=" + getValue() + '}';
  }

}
