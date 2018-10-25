package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.*;
import de.adito.util.weak.MixedReferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

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
  public void addWeakListener(@NotNull IPropertyPitEventListener pListener)
  {
    listeners.addWeak(pListener);
  }

  @Override
  public void addStrongListener(@NotNull IPropertyPitEventListener pListener)
  {
    listeners.addStrong(pListener);
  }

  @Override
  public void removeListener(@NotNull IPropertyPitEventListener pListener)
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

  protected void fireValueWillBeChanged(@NotNull IProperty pProperty, @Nullable Object pOldValue, @Nullable Object pNewValue,
                                        @NotNull Consumer<Runnable> pOnRemoved, @NotNull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyValueWillBeChanged(pProperty, pOldValue, pNewValue, pOnRemoved, pAttributes);
  }

  protected void fireValueChanged(@NotNull IProperty pProperty, @Nullable Object pOldValue, @Nullable Object pNewValue,
                                  @NotNull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyValueChanged(pProperty, pOldValue, pNewValue, pAttributes);
  }

  protected void firePropertyAdded(@NotNull IPropertyPitProvider pPropertyPitProvider, @NotNull IPropertyDescription pDescription,
                                   @NotNull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyAdded(pPropertyPitProvider, pDescription, pAttributes);
  }

  protected void firePropertyWillBeRemoved(@NotNull IProperty pProperty, @NotNull Consumer<Runnable> pOnRemoved,
                                           @NotNull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyWillBeRemoved(pProperty, pOnRemoved, pAttributes);
  }

  protected void firePropertyRemoved(@NotNull IPropertyPitProvider pPropertyPitProvider, @NotNull IPropertyDescription pDescription,
                                     @NotNull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyRemoved(pPropertyPitProvider, pDescription, pAttributes);
  }

  public void fireChildrenOrderWillBeChanged(@NotNull IPropertyPitProvider pPropertyPitProvider, @NotNull Consumer<Runnable> pOnRemoved,
                                             @NotNull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyOrderWillBeChanged(pPropertyPitProvider, pOnRemoved, pAttributes);
  }

  public void fireChildrenOrderChanged(@NotNull IPropertyPitProvider pPropertyPitProvider, @NotNull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyOrderChanged(pPropertyPitProvider, pAttributes);
  }

  public void firePropertyRenamed(@NotNull IProperty pProperty, @NotNull String pOldName, @NotNull String pNewName,
                                  @NotNull Set<Object> pAttributes)
  {
    for (IPropertyPitEventListener listener : listeners)
      //noinspection unchecked
      listener.propertyNameChanged(pProperty, pOldName, pNewName, pAttributes);
  }

}
