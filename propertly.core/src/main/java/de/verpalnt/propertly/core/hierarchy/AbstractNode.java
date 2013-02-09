package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 19:36
 */
public abstract class AbstractNode
{

  private final Hierarchy hierarchy;
  private final AbstractNode parent;
  private HierarchyProperty property;

  private List<IPropertyEventListener> listeners;


  protected AbstractNode(@Nonnull Hierarchy pHierarchy, @Nullable AbstractNode pParent,
                         @Nonnull IPropertyDescription pPropertyDescription)
  {
    hierarchy = pHierarchy;
    parent = pParent;
    property = new HierarchyProperty(this, pPropertyDescription);
  }

  public abstract Object setValue(Object pValue);

  public abstract Object getValue();

  @Nullable
  public abstract List<AbstractNode> getChildren();

  public abstract void addProperty(IPropertyDescription pPropertyDescription);

  public abstract boolean removeProperty(String pName);

  public Hierarchy getHierarchy()
  {
    return hierarchy;
  }

  public AbstractNode getParent()
  {
    return parent;
  }

  public String getPath()
  {
    AbstractNode parentNode = getParent();
    String name = getProperty().getName();
    return parentNode == null ? name : parentNode.getPath() + "/" + name;
  }

  public IProperty getProperty()
  {
    return property;
  }

  public void addPropertyEventListener(IPropertyEventListener pListener)
  {
    if (listeners == null)
      listeners = new ArrayList<IPropertyEventListener>();
    listeners.add(pListener);
  }

  public void removePropertyEventListener(IPropertyEventListener pListener)
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
      List<IPropertyEventListener> l = p.listeners;
      if (l != null)
        for (IPropertyEventListener eventListener : l)
          eventListener.propertyChange(getProperty(), pOldValue, pNewValue);
    }
    property.fire(pOldValue, pNewValue);
  }

  protected void fireNodeAdded(IPropertyDescription pPropertyDescription)
  {
    getHierarchy().firePropertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyEventListener listener : listeners)
        listener.propertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  protected void fireNodeRemoved(IPropertyDescription pPropertyDescription)
  {
    getHierarchy().firePropertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyEventListener listener : listeners)
        listener.propertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  protected AbstractNode find(String pName)
  {
    List<AbstractNode> cs = getChildren();
    if (cs != null)
      for (AbstractNode child : cs)
        if (pName.equals(child.getProperty().getName()))
          return child;
    return null;
  }

}
