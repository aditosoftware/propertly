package de.adito.propertly.core.hierarchy;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.PropertlyUtility;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 19:36
 */
public abstract class AbstractNode implements INode
{

  private final Hierarchy hierarchy;
  private final AbstractNode parent;
  private final HierarchyProperty property;

  private List<IPropertyPitEventListener> listeners;


  protected AbstractNode(@Nonnull Hierarchy pHierarchy, @Nullable AbstractNode pParent,
                         @Nonnull IPropertyDescription pPropertyDescription)
  {
    hierarchy = pHierarchy;
    parent = pParent;
    if (parent == null || parent.getValue() instanceof IMutablePropertyPitProvider)
      property = new DynamicHierarchyProperty(this, pPropertyDescription);
    else
      property = new HierarchyProperty(this, pPropertyDescription);
  }

  @Nonnull
  @Override
  public Hierarchy getHierarchy()
  {
    return hierarchy;
  }

  @Override
  public AbstractNode getParent()
  {
    return parent;
  }

  @Nonnull
  @Override
  public String getPath()
  {
    INode parentNode = getParent();
    String name = getProperty().getName();
    return parentNode == null ? name : parentNode.getPath() + "/" + name;
  }

  @Nonnull
  @Override
  public IProperty getProperty()
  {
    return property;
  }

  @Override
  public void addPropertyPitEventListener(@Nonnull IPropertyPitEventListener pListener)
  {
    if (listeners == null)
      listeners = new ArrayList<IPropertyPitEventListener>();
    listeners.add(pListener);
  }

  @Override
  public void removePropertyPitEventListener(@Nonnull IPropertyPitEventListener pListener)
  {
    if (listeners != null)
      listeners.add(pListener);
  }

  protected void fireValueChange(Object pOldValue, Object pNewValue)
  {
    getHierarchy().fireNodeChanged(getProperty(), pOldValue, pNewValue);
    AbstractNode p = getParent();
    if (p != null)
    {
      List<IPropertyPitEventListener> l = p.listeners;
      if (l != null)
        for (IPropertyPitEventListener eventListener : l)
          //noinspection unchecked
          eventListener.propertyChanged(getProperty(), pOldValue, pNewValue);
    }
    property.fire(pOldValue, pNewValue);
  }

  protected void fireNodeAdded(IPropertyDescription pPropertyDescription)
  {
    getHierarchy().firePropertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked
        listener.propertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  protected void fireNodeWillBeRemoved(IPropertyDescription pPropertyDescription)
  {
    getHierarchy().firePropertyWillBeRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked
        listener.propertyWillBeRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  protected void fireNodeRemoved(IPropertyDescription pPropertyDescription)
  {
    getHierarchy().firePropertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyPitEventListener listener : listeners)
        //noinspection unchecked
        listener.propertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  @Override
  public String toString()
  {
    return PropertlyUtility.asString(this, "path=" + getPath(), "value=" + getProperty().getValue());
  }

}
