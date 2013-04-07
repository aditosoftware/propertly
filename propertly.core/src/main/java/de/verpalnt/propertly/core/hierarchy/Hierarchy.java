package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Hierarchy<T extends IPropertyPitProvider>
{

  private final INode node;
  private final List<IPropertyEventListener> listeners;


  public Hierarchy(String pName, T pPPP)
  {
    this(pName, pPPP, null);
  }

  protected Hierarchy(String pName, T pPPP, Object pExtra)
  {
    node = createNode(pName, pExtra);
    listeners = new ArrayList<IPropertyEventListener>();
    node.setValue(pPPP);
  }

  public IProperty<IPropertyPitProvider, T> getProperty()
  {
    //noinspection unchecked
    return getNode().getProperty();
  }

  public T getValue()
  {
    return getProperty().getValue();
  }

  public void addPropertyEventListener(IPropertyEventListener pListener)
  {
    listeners.add(pListener);
  }

  public void removePropertyEventListener(IPropertyEventListener pListener)
  {
    listeners.remove(pListener);
  }

  protected INode getNode()
  {
    return node;
  }

  protected INode createNode(String pName, Object pExtra)
  {
    return new Node(this, null, PropertyDescription.create(IPropertyPitProvider.class, IPropertyPitProvider.class,
        pName, Collections.<Annotation>emptySet()));
  }

  protected void fireNodeChanged(IProperty pProperty, Object pOldValue, Object pNewValue)
  {
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyChange(pProperty, pOldValue, pNewValue);
  }

  protected void firePropertyAdded(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyAdded(pPropertyPitProvider, pDescription);
  }

  protected void firePropertyWillBeRemoved(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyWillBeRemoved(pPropertyPitProvider, pDescription);
  }

  protected void firePropertyRemoved(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyRemoved(pPropertyPitProvider, pDescription);
  }

}
