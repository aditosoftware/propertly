package de.adito.propertly.reactive.impl;

import de.adito.propertly.core.common.PropertyPitEventAdapter;
import de.adito.propertly.core.spi.*;
import de.adito.util.reactive.AbstractListenerObservable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author w.glanzer, 27.04.2018
 */
class PropertyPitObservable<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    extends AbstractListenerObservable<IPropertyPitEventListener<S, T>, S, S>
{

  private final boolean completeWhenInvalid;

  PropertyPitObservable(S pPropertyPitProvider, boolean pCompleteWhenInvalid)
  {
    super(pPropertyPitProvider);
    completeWhenInvalid = pCompleteWhenInvalid;
  }

  @NotNull
  @Override
  protected IPropertyPitEventListener<S, T> registerListener(@NotNull S pListenableValue, @NotNull IFireable<S> pFireable)
  {
    IPropertyPitEventListener<S, T> listener = new _Listener<>(pFireable, pListenableValue, completeWhenInvalid);
    pListenableValue.getPit().addStrongListener(listener);
    return listener;
  }

  @Override
  protected void removeListener(@NotNull S pListenableValue, @NotNull IPropertyPitEventListener<S, T> pListener)
  {
    IPropertyPit<P, S, T> pit = pListenableValue.getPit();
    if(pit.isValid()) // If this pit is invalid, the listener was already removed
      pit.removeListener(pListener);
  }

  /**
   * Listener-Impl
   */
  private static class _Listener<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
      extends PropertyPitEventAdapter<S, T>
  {
    private final IFireable<S> fireable;
    private final IPropertyPitProvider<P, S, T> pit;
    private final boolean completeWhenInvalid;

    public _Listener(IFireable<S> pFireable, S pPit, boolean pCompleteWhenInvalid)
    {
      fireable = pFireable;
      pit = pPit;
      completeWhenInvalid = pCompleteWhenInvalid;
    }

    @Override
    public void propertyRemoved(@NotNull S pSource, @NotNull IPropertyDescription<S, T> pPropertyDescription, @NotNull Set<Object> pAttributes)
    {
      fireable.fireValueChanged(pSource);
    }

    @Override
    public void propertyAdded(@NotNull S pSource, @NotNull IPropertyDescription<S, T> pPropertyDescription, @NotNull Set<Object> pAttributes)
    {
      fireable.fireValueChanged(pSource);
    }

    @Override
    public void propertyOrderChanged(@NotNull S pSource, @NotNull Set<Object> pAttributes)
    {
      fireable.fireValueChanged(pSource);
    }

    @Override
    public void propertyWillBeRemoved(@NotNull IProperty<S, T> pProperty, @NotNull Consumer<Runnable> pOnRemoved, @NotNull Set<Object> pAttributes)
    {
      if (completeWhenInvalid && Objects.equals(pProperty.getValue(), pit))
        fireable.fireCompleted();
    }
  }
}
