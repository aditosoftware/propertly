package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author j.boesl, 30.10.14
 */
abstract class AbstractPropertyPitProviderBase<S extends IPropertyPitProvider>
    implements IPropertyPitProvider<S>, Iterable<IProperty<S, ?>>
{

  @Nullable
  public IPropertyPitProvider getParent()
  {
    return getPit().getParent();
  }

  @Nullable
  <T> IProperty<S, T> findProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getPit().findProperty(pPropertyDescription);
  }

  @Nonnull
  public <T> IProperty<S, T> getProperty(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getPit().getProperty(pPropertyDescription);
  }

  @Nullable
  public <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getPit().getValue(pPropertyDescription);
  }

  @Nullable
  public <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue)
  {
    return getPit().setValue(pPropertyDescription, pValue);
  }

  public Set<IPropertyDescription> getPropertyDescriptions()
  {
    return getPit().getPropertyDescriptions();
  }

  @Nonnull
  public List<IProperty<S, ?>> getProperties()
  {
    return getPit().getProperties();
  }

  @Override
  public Iterator<IProperty<S, ?>> iterator()
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
