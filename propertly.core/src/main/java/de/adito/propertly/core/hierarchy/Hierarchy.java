package de.adito.propertly.core.hierarchy;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.IFunction;

import java.util.*;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Hierarchy<T extends IPropertyPitProvider>
{

  private final INode node;
  private final List<IPropertyPitEventListener> listeners;


  public Hierarchy(final String pName, T pPPP)
  {
    this(new IFunction<Hierarchy, INode>()
    {
      @Override
      public INode run(Hierarchy pHierarchy)
      {
        return new Node(pHierarchy, null, PropertyDescription.create(
            IPropertyPitProvider.class, IPropertyPitProvider.class, pName));
      }
    }, pPPP);

  }

  protected Hierarchy(IFunction<Hierarchy, INode> pNodeSupplier, T pPPP)
  {
    node = pNodeSupplier.run(this);
    listeners = new ArrayList<IPropertyPitEventListener>();
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

  public void addPropertyPitEventListener(IPropertyPitEventListener pListener)
  {
    listeners.add(pListener);
  }

  public void removePropertyPitEventListener(IPropertyPitEventListener pListener)
  {
    listeners.remove(pListener);
  }

  protected INode getNode()
  {
    return node;
  }

  protected void fireNodeChanged(IProperty pProperty, Object pOldValue, Object pNewValue)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyChanged(pProperty, pOldValue, pNewValue);
  }

  protected void firePropertyAdded(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyAdded(pPropertyPitProvider, pDescription);
  }

  protected void firePropertyWillBeRemoved(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyWillBeRemoved(pPropertyPitProvider, pDescription);
  }

  protected void firePropertyRemoved(IPropertyPitProvider pPropertyPitProvider, IPropertyDescription pDescription)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyRemoved(pPropertyPitProvider, pDescription);
  }

}
