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

  private final Node node;
  private final List<IPropertyEventListener> listeners;

  private Hierarchy(String pName)
  {
    node = new Node(this, null, PropertyDescription.create(IPropertyPitProvider.class, IPropertyPitProvider.class,
        pName, Collections.<Annotation>emptySet()));
    listeners = new ArrayList<IPropertyEventListener>();
  }

  public Hierarchy(String pName, T pPPP)
  {
    this(pName);
    node.setValue(pPPP);
  }

  public T getValue()
  {
    return (T) node.getValue();
  }

  public void addPropertyEventListener(IPropertyEventListener pListener)
  {
    listeners.add(pListener);
  }

  public void removePropertyEventListener(IPropertyEventListener pListener)
  {
    listeners.remove(pListener);
  }

  void fireNodeChanged(IProperty pProperty, Object pOldValue, Object pNewValue)
  {
    for (IPropertyEventListener listener : listeners)
      listener.propertyChange(pProperty, pOldValue, pNewValue);
  }

  void firePropertyAdded(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyEventListener listener : listeners)
      listener.propertyAdded(pPropertyPitProvider, pDescription);
  }

  void firePropertyRemoved(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyEventListener listener : listeners)
      listener.propertyRemoved(pPropertyPitProvider, pDescription);
  }

}
