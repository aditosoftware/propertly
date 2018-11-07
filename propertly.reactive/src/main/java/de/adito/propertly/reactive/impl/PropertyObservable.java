package de.adito.propertly.reactive.impl;

import de.adito.propertly.core.common.PropertyEventAdapter;
import de.adito.propertly.core.spi.*;
import de.adito.util.reactive.AbstractListenerObservable;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Observable that encapsulates an property and fire as value if
 * propertyValue or propertyName change and complete if property will become invalid.
 *
 * @author w.glanzer, 23.04.2018
 */
class PropertyObservable<P extends IPropertyPitProvider, V>
    extends AbstractListenerObservable<IPropertyEventListener<P, V>, IProperty<P, V>, IProperty<P, V>>
{
  private final boolean completeWhenInvalid;

  PropertyObservable(IProperty<P, V> pProperty, boolean pCompleteWhenInvalid)
  {
    super(pProperty);
    completeWhenInvalid = pCompleteWhenInvalid;
  }

  @NotNull
  @Override
  protected IPropertyEventListener<P, V> registerListener(@NotNull IProperty<P, V> pListenableValue, @NotNull IFireable<IProperty<P, V>> pFireable)
  {
    IPropertyEventListener<P, V> listener = new _Listener<>(pFireable, completeWhenInvalid);
    pListenableValue.addStrongListener(listener);
    return listener;
  }

  @Override
  protected void removeListener(@NotNull IProperty<P, V> pListenableValue, @NotNull IPropertyEventListener<P, V> pListener)
  {
    pListenableValue.removeListener(pListener);
  }

  /**
   * Listener-Impl
   */
  private static class _Listener<P extends IPropertyPitProvider, V> extends PropertyEventAdapter<P, V>
  {
    private final IFireable<IProperty<P, V>> fireable;
    private final boolean completeWhenInvalid;

    public _Listener(IFireable<IProperty<P, V>> pFireable, boolean pCompleteWhenInvalid)
    {
      fireable = pFireable;
      completeWhenInvalid = pCompleteWhenInvalid;
    }

    @Override
    public void propertyValueChanged(@NotNull IProperty<P, V> pProperty, V pOldValue, V pNewValue, @NotNull Set<Object> pAttributes)
    {
      fireable.fireValueChanged(pProperty);
    }

    @Override
    public void propertyNameChanged(@NotNull IProperty<P, V> pProperty, @NotNull String pOldName, @NotNull String pNewName, @NotNull Set<Object> pAttributes)
    {
      fireable.fireValueChanged(pProperty);
    }

    @Override
    public void propertyWillBeRemoved(@NotNull IProperty<P, V> pProperty, @NotNull Consumer<Runnable> pOnRemoved, @NotNull Set<Object> pAttributes)
    {
      if (completeWhenInvalid)
      {
        // Property will be invalid after remove -> complete observable
        fireable.fireCompleted();
      }
    }
  }
}
