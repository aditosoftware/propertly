package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author PaL
 *         Date: 07.02.13
 *         Time: 21:44
 */
public abstract class AbstractMutablePPP<S extends IMutablePropertyPitProvider, T> implements IMutablePropertyPitProvider, Iterable<IProperty<S, ?>>
{

  private final IMutablePropertyPit<S, T> pit;


  protected AbstractMutablePPP(IMutablePropertyPitFactory pFactory, Class<T> pAllowedChildType)
  {
    //noinspection unchecked
    pit = pFactory.create((S) this, pAllowedChildType);
  }

  @Override
  public final IMutablePropertyPit<S, T> getPit()
  {
    return pit;
  }

  @Nonnull
  public <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> getProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    return pit.getProperty(pPropertyDescription);
  }

  public boolean removeProperty(IPropertyDescription<S, T> pPropertyDescription)
  {
    return pit.removeProperty(pPropertyDescription);
  }

  @Nullable
  public IPropertyPitProvider getParent()
  {
    return pit.getParent();
  }

  @Nullable
  public <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue)
  {
    return pit.setValue(pPropertyDescription, pValue);
  }

  public void removePropertyEventListener(IPropertyEventListener pListener)
  {
    pit.removePropertyEventListener(pListener);
  }

  public Class<T> getChildType()
  {
    return pit.getChildType();
  }

  @Nullable
  public <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> findProperty(IPropertyDescription<SOURCE, T> pPropertyDescription)
  {
    return pit.findProperty(pPropertyDescription);
  }

  public void addPropertyEventListener(IPropertyEventListener pListener)
  {
    pit.addPropertyEventListener(pListener);
  }

  @Nonnull
  public IProperty<S, T> addProperty(@Nonnull Class<T> pType, @Nonnull String pName, @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    return pit.addProperty(pType, pName, pAnnotations);
  }

  @Nonnull
  public IProperty<S, T> addProperty(IPropertyDescription<S, T> pPropertyDescription)
  {
    return pit.addProperty(pPropertyDescription);
  }

  @Nullable
  public <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return pit.getValue(pPropertyDescription);
  }

  @Nonnull
  public List<IProperty<S, ?>> getProperties()
  {
    return pit.getProperties();
  }

  public Set<IPropertyDescription> getPropertyDescriptions()
  {
    return pit.getPropertyDescriptions();
  }

  @Override
  public Iterator<IProperty<S, ?>> iterator()
  {
    return pit.iterator();
  }
}
