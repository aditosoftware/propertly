package de.adito.propertly.core.spi.extension;

import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitEventListener;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author j.boesl, 30.10.14
 */
abstract class AbstractPropertyPitProviderBase<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    implements IPropertyPitProvider<P, S, T>, Iterable<IProperty<S, T>>
{
  @NotNull
  public S getSource()
  {
    return getPit().getSource();
  }

  public boolean isValid()
  {
    return getPit().isValid();
  }

  @NotNull
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

  @Nullable
  public IProperty<S, T> findProperty(@NotNull String pName)
  {
    return getPit().findProperty(pName);
  }

  @NotNull
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

  @NotNull
  public Set<IPropertyDescription<S, T>> getPropertyDescriptions()
  {
    return getPit().getPropertyDescriptions();
  }

  @NotNull
  public List<IProperty<S, T>> getProperties()
  {
    return getPit().getProperties();
  }

  public Class<T> getChildType()
  {
    return getPit().getChildType();
  }

  @NotNull
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
  public void addWeakListener(@NotNull IPropertyPitEventListener pListener)
  {
    getPit().addWeakListener(pListener);
  }

  /**
   * Adds a strong listener.
   *
   * @param pListener the listener to be strongly added.
   */
  public void addStrongListener(@NotNull IPropertyPitEventListener pListener)
  {
    getPit().addStrongListener(pListener);
  }

  /**
   * Removes a listener.
   *
   * @param pListener the listener to be removed.
   */
  public void removeListener(@NotNull IPropertyPitEventListener pListener)
  {
    getPit().removeListener(pListener);
  }

}
