package de.adito.propertly.core.api;

import de.adito.propertly.core.common.IFunction;
import de.adito.propertly.core.common.ListenerList;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitEventListener;
import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.Nonnull;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Hierarchy<T extends IPropertyPitProvider>
{

  private final INode node;
  private final ListenerList<IPropertyPitEventListener> listeners;


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
    listeners = new ListenerList<IPropertyPitEventListener>();
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

  /**
   * Adds a weak listener.
   *
   * @param pListener the listener to be weakly added.
   */
  public void addWeakListener(@Nonnull IPropertyPitEventListener pListener)
  {
    listeners.addWeakListener(pListener);
  }

  /**
   * Adds a strong listener.
   *
   * @param pListener the listener to be strongly added.
   */
  public void addStrongListener(@Nonnull IPropertyPitEventListener pListener)
  {
    listeners.addStrongListener(pListener);
  }

  /**
   * Removes a listener.
   *
   * @param pListener the listener to be removed.
   */
  public void removeListener(@Nonnull IPropertyPitEventListener pListener)
  {
    listeners.removeListener(pListener);
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
