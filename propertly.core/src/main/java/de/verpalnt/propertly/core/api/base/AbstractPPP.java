package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 21:32
 */
public abstract class AbstractPPP<S extends IPropertyPitProvider> implements IPropertyPitProvider, Iterable<IProperty<S, ?>>
{
  private final IPropertyPit<S> pit;

  protected AbstractPPP(IPropertyPitFactory pFactory)
  {
    //noinspection unchecked
    pit = pFactory.create((S) this);
  }

  @Override
  public final IPropertyPit<S> getPit()
  {
    return pit;
  }

  @Nullable
  public IPropertyPitProvider getParent()
  {
    return pit.getParent();
  }

  @Nullable
  public <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> findProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    return pit.findProperty(pPropertyDescription);
  }

  @Nonnull
  public <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> getProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    return pit.getProperty(pPropertyDescription);
  }

  @Nullable
  public <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return pit.getValue(pPropertyDescription);
  }

  @Nullable
  public <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue)
  {
    return pit.setValue(pPropertyDescription, pValue);
  }

  public Set<IPropertyDescription> getPropertyDescriptions()
  {
    return pit.getPropertyDescriptions();
  }

  @Nonnull
  public List<IProperty<S, ?>> getProperties()
  {
    return pit.getProperties();
  }

  @Override
  public Iterator<IProperty<S, ?>> iterator()
  {
    return pit.iterator();
  }

  public void addPropertyEventListener(IPropertyEventListener pListener)
  {
    pit.addPropertyEventListener(pListener);
  }

  public void removePropertyEventListener(IPropertyEventListener pListener)
  {
    pit.removePropertyEventListener(pListener);
  }
}
