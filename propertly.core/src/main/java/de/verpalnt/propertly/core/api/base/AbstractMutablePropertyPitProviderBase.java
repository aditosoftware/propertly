package de.verpalnt.propertly.core.api.base;

import de.verpalnt.propertly.core.api.*;

import javax.annotation.*;
import java.lang.annotation.Annotation;

/**
 * @author j.boesl, 30.10.14
 */
public abstract class AbstractMutablePropertyPitProviderBase<S extends IMutablePropertyPitProvider, T>
    extends AbstractPropertyPitProviderBase<S>
    implements IMutablePropertyPitProvider<S, T>
{

  @Override
  public abstract IMutablePropertyPit<S, T> getPit();

  public Class<T> getChildType()
  {
    return getPit().getChildType();
  }

  public boolean removeProperty(IPropertyDescription<S, T> pPropertyDescription)
  {
    return getPit().removeProperty(pPropertyDescription);
  }

  @Nonnull
  public <E extends T> IProperty<S, E> addProperty(@Nonnull E pValue)
  {
    return getPit().addProperty(pValue);
  }

  @Nonnull
  public <E extends T> IProperty<S, E> addProperty(@Nonnull String pName, @Nonnull E pValue)
  {
    return getPit().addProperty(pName, pValue);
  }

  @Nonnull
  public <E extends T> IProperty<S, E> addProperty(@Nonnull IPropertyDescription<S, E> pPropertyDescription)
  {
    return getPit().addProperty(pPropertyDescription);
  }

  @Nonnull
  public <E extends T> IProperty<S, E> addProperty(@Nonnull Class<E> pType, @Nonnull String pName,
                                                   @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    return getPit().addProperty(pType, pName, pAnnotations);
  }
}
