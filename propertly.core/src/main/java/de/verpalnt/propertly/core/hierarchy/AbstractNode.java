package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.common.PropertlyUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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

  private List<IPropertyEventListener> listeners;


  protected AbstractNode(@Nonnull Hierarchy pHierarchy, @Nullable AbstractNode pParent,
                         @Nonnull IPropertyDescription pPropertyDescription)
  {
    hierarchy = pHierarchy;
    parent = pParent;
    property = new HierarchyProperty(this, pPropertyDescription);
  }

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

  @Override
  public String getPath()
  {
    INode parentNode = getParent();
    String name = getProperty().getName();
    return parentNode == null ? name : parentNode.getPath() + "/" + name;
  }

  @Override
  public IProperty getProperty()
  {
    return property;
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
          //noinspection unchecked
          eventListener.propertyChange(getProperty(), pOldValue, pNewValue);
    }
    property.fire(pOldValue, pNewValue);
  }

  protected void fireNodeAdded(IPropertyDescription pPropertyDescription)
  {
    getHierarchy().firePropertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyEventListener listener : listeners)
        //noinspection unchecked
        listener.propertyAdded((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  protected void fireNodeWillBeRemoved(IPropertyDescription pPropertyDescription)
  {
    getHierarchy().firePropertyWillBeRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyEventListener listener : listeners)
        //noinspection unchecked
        listener.propertyWillBeRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  protected void fireNodeRemoved(IPropertyDescription pPropertyDescription)
  {
    getHierarchy().firePropertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
    if (listeners != null)
      for (IPropertyEventListener listener : listeners)
        //noinspection unchecked
        listener.propertyRemoved((IPropertyPitProvider) getValue(), pPropertyDescription);
  }

  protected INode find(String pName)
  {
    List<INode> cs = getChildren();
    if (cs != null)
      for (INode child : cs)
        if (pName.equals(child.getProperty().getName()))
          return child;
    return null;
  }

  @Override
  public String toString()
  {
    return PropertlyUtility.asString(this, "path=" + getPath(), "value=" + getProperty().getValue());
  }

}
