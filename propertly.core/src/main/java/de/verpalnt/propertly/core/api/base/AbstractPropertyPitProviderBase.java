package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author j.boesl, 30.10.14
 */
abstract class AbstractPropertyPitProviderBase<S extends IPropertyPitProvider, T>
    implements IPropertyPitProvider<S, T>, Iterable<IProperty<S, T>>
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
  public IProperty<?, S> getOwnProperty()
  {
    return getPit().getOwnProperty();
  }

  @Nullable
  public IPropertyPitProvider getParent()
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

  public void addPropertyEventListener(IPropertyEventListener pListener)
  {
    getPit().addPropertyEventListener(pListener);
  }

  public void removePropertyEventListener(IPropertyEventListener pListener)
  {
    getPit().removePropertyEventListener(pListener);
  }

}
