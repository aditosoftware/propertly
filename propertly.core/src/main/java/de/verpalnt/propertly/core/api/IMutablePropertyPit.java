package de.verpalnt.propertly.core.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * Created by PaL on 09.11.13.
 */
public interface IMutablePropertyPit<S extends IMutablePropertyPitProvider, T> extends IPropertyPit<S>, IMutablePropertyPitProvider<S, T>
{
  @Nonnull
  IProperty<S, T> addProperty(IPropertyDescription<S, T> pPropertyDescription);

  @Nonnull
  IProperty<S, T> addProperty(@Nonnull Class<T> pType, @Nonnull String pName,
                              @Nullable Iterable<? extends Annotation> pAnnotations);

  boolean removeProperty(IPropertyDescription<S, T> pPropertyDescription);

  Class<T> getChildType();
}
