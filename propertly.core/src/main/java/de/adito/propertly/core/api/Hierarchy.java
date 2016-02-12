package de.adito.propertly.core.api;

import de.adito.propertly.core.common.ListenerList;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.Function;

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
    this(pHierarchy -> {
      return new Node(pHierarchy, null, PropertyDescription.create(
          IPropertyPitProvider.class, pPPP.getClass(), pName));
    }, pPPP);

  }

  protected Hierarchy(Function<Hierarchy, INode> pNodeSupplier, T pPPP)
  {
    node = pNodeSupplier.apply(this);
    listeners = new ListenerList<>();
    node.setValue(pPPP, Collections.emptySet());
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

  protected void fireNodeChanged(@Nonnull IProperty pProperty, @Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyChanged(pProperty, pOldValue, pNewValue, pAttributes);
  }

  protected void firePropertyAdded(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull IPropertyDescription pDescription, @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyAdded(pPropertyPitProvider, pDescription, pAttributes);
  }

  protected void firePropertyWillBeRemoved(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull IPropertyDescription pDescription, @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyWillBeRemoved(pPropertyPitProvider, pDescription, pAttributes);
  }

  protected void firePropertyRemoved(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull IPropertyDescription pDescription, @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyRemoved(pPropertyPitProvider, pDescription, pAttributes);
  }

  public void fireChildrenOrderChanged(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyOrderChanged(pPropertyPitProvider, pAttributes);
  }

  public void fireNodeRenamed(@Nonnull IProperty pProperty, @Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyNameChanged(pProperty, pOldName, pNewName, pAttributes);
  }

}
