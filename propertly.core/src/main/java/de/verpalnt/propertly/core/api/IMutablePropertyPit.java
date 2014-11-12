package de.verpalnt.propertly.core.api;


import javax.annotation.*;
import java.lang.annotation.Annotation;

/**
 * @author PaL, 09.11.13
 */
public interface IMutablePropertyPit<S extends IMutablePropertyPitProvider, T>
    extends IPropertyPit<S, T>, IMutablePropertyPitProvider<S, T>
{
  @Nonnull
  <E extends T> IProperty<S, E> addProperty(@Nonnull E pValue);

  @Nonnull
  <E extends T> IProperty<S, E> addProperty(@Nonnull String pName, @Nonnull E pValue);

  @Nonnull
  <E extends T> IProperty<S, E> addProperty(@Nonnull IPropertyDescription<S, E> pPropertyDescription);

  @Nonnull
  <E extends T> IProperty<S, E> addProperty(@Nonnull Class<E> pType, @Nonnull String pName,
                                            @Nullable Iterable<? extends Annotation> pAnnotations);

  boolean removeProperty(@Nonnull IPropertyDescription<? super S, T> pPropertyDescription);

  boolean removeProperty(@Nonnull IProperty<S, T> pProperty);

  Class<T> getChildType();
}
