package de.adito.propertly.core.spi.extension;

import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author j.boesl, 30.10.14
 */
abstract class AbstractPropertyPitProviderBase<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    implements IPropertyPitProvider<P, S, T>, Iterable<IProperty<S, T>>
{
  @Nonnull
  public S getSource()
  {
    return getPit().getSource();
  }

  public boolean isValid()
  {
    return getPit().isValid();
  }

  @Nonnull
  public IProperty<P, S> getOwnProperty()
  {
    return getPit().getOwnProperty();
  }

  @Nullable
  public P getParent()
  {
    return getPit().getParent();
  }

  @Nullable
  public <E extends T> IProperty<S, E> findProperty(IPropertyDescription<?, E> pPropertyDescription)
  {
    return getPit().findProperty(pPropertyDescription);
  }

  @Nonnull
  public <E extends T> IProperty<S, E> getProperty(IPropertyDescription<? super S, E> pPropertyDescription)
  {
    return getPit().getProperty(pPropertyDescription);
  }

  @Nullable
  public <E extends T> E getValue(IPropertyDescription<? super S, E> pPropertyDescription)
  {
    return getPit().getValue(pPropertyDescription);
  }

  @Nullable
  public <E extends T> E setValue(IPropertyDescription<? super S, E> pPropertyDescription, E pValue)
  {
    return getPit().setValue(pPropertyDescription, pValue);
  }

  @Nonnull
  public Set<IPropertyDescription<S, T>> getPropertyDescriptions()
  {
    return getPit().getPropertyDescriptions();
  }

  @Nonnull
  public List<IProperty<S, T>> getProperties()
  {
    return getPit().getProperties();
  }

  public Class<T> getChildType()
  {
    return getPit().getChildType();
  }

  @Nonnull
  public List<T> getValues()
  {
    return getPit().getValues();
  }

  @Override
  public Iterator<IProperty<S, T>> iterator()
  {
    return getPit().iterator();
  }

  /**
   * Adds a weak listener.
   *
   * @param pListener the listener to be weakly added.
   */
  public void addWeakListener(@Nonnull IPropertyPitEventListener pListener)
  {
    getPit().addWeakListener(pListener);
  }

  /**
   * Adds a strong listener.
   *
   * @param pListener the listener to be strongly added.
   */
  public void addStrongListener(@Nonnull IPropertyPitEventListener pListener)
  {
    getPit().addStrongListener(pListener);
  }

  /**
   * Removes a listener.
   *
   * @param pListener the listener to be removed.
   */
  public void removeListener(@Nonnull IPropertyPitEventListener pListener)
  {
    getPit().removeListener(pListener);
  }

}
