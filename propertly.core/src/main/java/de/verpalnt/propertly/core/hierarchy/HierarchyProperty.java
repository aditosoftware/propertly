package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PaL
 *         Date: 30.01.13
 *         Time: 00:30
 */
class HierarchyProperty implements IProperty
{

  private AbstractNode node;
  private IPropertyDescription propertyDescription;
  private List<IPropertyEventListener> listeners;

  HierarchyProperty(AbstractNode pNode, IPropertyDescription pPropertyDescription)
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
  public IPropertyPitProvider getParent()
  {
    AbstractNode parent = node.getParent();
    return parent == null ? null : (IPropertyPitProvider) parent.getValue();
  }

  @Override
  public Class getSourceType()
  {
    return propertyDescription.getSourceType();
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
  public void addPropertyEventListener(IPropertyEventListener pListener)
  {
    if (listeners == null)
      listeners = new ArrayList<IPropertyEventListener>();
    listeners.add(pListener);
  }

  @Override
  public void removePropertyEventListener(IPropertyEventListener pListener)
  {
    if (listeners != null)
      listeners.remove(pListener);
  }

  void fire(Object pOldValue, Object pNewValue)
  {
    if (listeners != null)
      for (IPropertyEventListener listener : listeners)
        listener.propertyChange(this, pOldValue, pNewValue);
  }

  AbstractNode getNode()
  {
    return node;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "{" + propertyDescription + ", value=" + getValue() + '}';
  }

}
