package de.adito.propertly.core.api;

import de.adito.propertly.core.common.*;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Hierarchy<T extends IPropertyPitProvider> implements IHierarchy<T>
{

  private final INode node;
  private final ListenerList<IPropertyPitEventListener> listeners;


  /**
   * Instantiates this Hierarchy.
   *
   * @param pName the name for the root IProperty.
   * @param pPPP  the root IPropertyPitProvider.
   */
  public Hierarchy(final String pName, final T pPPP)
  {
    this(new IFunction<Hierarchy, INode>()
    {
      @Override
      public INode run(Hierarchy pHierarchy)
      {
        return new Node(pHierarchy, null, PropertyDescription.create(
            IPropertyPitProvider.class, pPPP.getClass(), pName));
      }
    }, pPPP);

  }

  protected Hierarchy(IFunction<Hierarchy, INode> pNodeSupplier, T pPPP)
  {
    node = pNodeSupplier.run(this);
    listeners = new ListenerList<IPropertyPitEventListener>();
    node.setValue(pPPP, Collections.emptyList());
  }

  @Override
  public IProperty<IPropertyPitProvider, T> getProperty()
  {
    //noinspection unchecked
    return getNode().getProperty();
  }

  @Override
  public T getValue()
  {
    return getProperty().getValue();
  }

  @Override
  public void addWeakListener(@Nonnull IPropertyPitEventListener pListener)
  {
    listeners.addWeakListener(pListener);
  }

  @Override
  public void addStrongListener(@Nonnull IPropertyPitEventListener pListener)
  {
    listeners.addStrongListener(pListener);
  }

  @Override
  public void removeListener(@Nonnull IPropertyPitEventListener pListener)
  {
    listeners.removeListener(pListener);
  }

  protected INode getNode()
  {
    return node;
  }

  protected void fireNodeChanged(@Nonnull IProperty pProperty, @Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull List<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyChanged(pProperty, pOldValue, pNewValue, pAttributes);
  }

  protected void firePropertyAdded(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull IPropertyDescription pDescription, @Nonnull List<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyAdded(pPropertyPitProvider, pDescription, pAttributes);
  }

  protected void firePropertyWillBeRemoved(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull IPropertyDescription pDescription, @Nonnull List<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyWillBeRemoved(pPropertyPitProvider, pDescription, pAttributes);
  }

  protected void firePropertyRemoved(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull IPropertyDescription pDescription, @Nonnull List<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyRemoved(pPropertyPitProvider, pDescription, pAttributes);
  }

  public void fireChildrenOrderChanged(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull List<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyOrderChanged(pPropertyPitProvider, pAttributes);
  }

  public void fireNodeRenamed(@Nonnull IProperty pProperty, @Nonnull String pOldName, @Nonnull String pNewName, @Nonnull List<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyNameChanged(pProperty, pOldName, pNewName, pAttributes);
  }

}
