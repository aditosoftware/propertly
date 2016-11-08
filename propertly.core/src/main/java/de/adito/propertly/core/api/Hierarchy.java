package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import de.adito.util.weak.MixedReferences;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:13
 */
public class Hierarchy<T extends IPropertyPitProvider> implements IHierarchy<T>
{

  private final INode node;
  private final MixedReferences<IPropertyPitEventListener> listeners;


  /**
   * Instantiates this Hierarchy.
   *
   * @param pName the name for the root IProperty.
   * @param pPPP  the root IPropertyPitProvider.
   */
  public Hierarchy(final String pName, final T pPPP)
  {
    this(pHierarchy -> {
      PropertyDescription<?, ?> description = new PropertyDescription<>(IPropertyPitProvider.class, pPPP.getClass(), pName);
      return new Node(pHierarchy, null, description, true);
    }, pPPP);
  }

  protected Hierarchy(Function<Hierarchy, INode> pNodeSupplier, T pPPP)
  {
    this(pNodeSupplier);
    node.setValue(pPPP, Collections.emptySet());
  }

  Hierarchy(Function<Hierarchy, INode> pNodeSupplier)
  {
    node = pNodeSupplier.apply(this);
    listeners = new MixedReferences<>();
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
    listeners.addWeak(pListener);
  }

  @Override
  public void addStrongListener(@Nonnull IPropertyPitEventListener pListener)
  {
    listeners.addStrong(pListener);
  }

  @Override
  public void removeListener(@Nonnull IPropertyPitEventListener pListener)
  {
    listeners.remove(pListener);
  }

  protected INode getNode()
  {
    return node;
  }

  protected INode getNode(IProperty pProperty)
  {
    return equals(pProperty.getHierarchy()) ? HierarchyHelper.getNode(pProperty) : null;
  }

  protected void fireValueWillBeChanged(@Nonnull IProperty pProperty, @Nullable Object pOldValue, @Nullable Object pNewValue,
                                        @Nonnull Consumer<Runnable> pOnRemoved, @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyValueWillBeChanged(pProperty, pOldValue, pNewValue, pOnRemoved, pAttributes);
  }

  protected void fireValueChanged(@Nonnull IProperty pProperty, @Nullable Object pOldValue, @Nullable Object pNewValue,
                                  @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyValueChanged(pProperty, pOldValue, pNewValue, pAttributes);
  }

  protected void firePropertyAdded(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull IPropertyDescription pDescription,
                                   @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyAdded(pPropertyPitProvider, pDescription, pAttributes);
  }

  protected void firePropertyWillBeRemoved(@Nonnull IProperty pProperty, @Nonnull Consumer<Runnable> pOnRemoved,
                                           @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyWillBeRemoved(pProperty, pOnRemoved, pAttributes);
  }

  protected void firePropertyRemoved(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull IPropertyDescription pDescription,
                                     @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyRemoved(pPropertyPitProvider, pDescription, pAttributes);
  }

  public void fireChildrenOrderWillBeChanged(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull Consumer<Runnable> pOnRemoved,
                                             @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyOrderWillBeChanged(pPropertyPitProvider, pOnRemoved, pAttributes);
  }

  public void fireChildrenOrderChanged(@Nonnull IPropertyPitProvider pPropertyPitProvider, @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyOrderChanged(pPropertyPitProvider, pAttributes);
  }

  public void firePropertyRenamed(@Nonnull IProperty pProperty, @Nonnull String pOldName, @Nonnull String pNewName,
                                  @Nonnull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyNameChanged(pProperty, pOldName, pNewName, pAttributes);
  }

}
