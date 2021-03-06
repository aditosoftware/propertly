package de.adito.propertly.core.spi;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

/**
 * An IPropertyPit where extra IProperty objects can be dynamically added and removed. Static IProperty objects can
 * still be available.
 *
 * @author PaL, 09.11.13
 */
public interface IMutablePropertyPit<P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
    extends IPropertyPit<P, S, T>, IMutablePropertyPitProvider<P, S, T>
{

  /**
   * Adds a new IProperty to this IMutablePropertyPit. The created IProperty is returned. The name for the IProperty is
   * an UUID. The type is used from the given value so the value mustn't be null.
   *
   * @param pValue the value for the IProperty. Defines the type, too.
   * @param <E>    the created IProperty's type.
   * @return the created IProperty.
   */
  @NotNull
  <E extends T> IProperty<S, E> addProperty(@NotNull E pValue);

  /**
   * Adds a new IProperty to this IMutablePropertyPit. The created IProperty is returned. The type is used from the
   * given value so the value mustn't be null.
   *
   * @param pName  the name for the new IProperty.
   * @param pValue the value for the IProperty. Defines the type, too.
   * @param <E>    the created IProperty's type.
   * @return the created IProperty.
   */
  @NotNull
  <E extends T> IProperty<S, E> addProperty(@NotNull String pName, @NotNull E pValue);

  /**
   * Adds a new IProperty to this IMutablePropertyPit based on the passed IPropertyDescription.
   *
   * @param pPropertyDescription the IPropertyDescription describing the new IProperty.
   * @param <E>                  the created IProperty's type.
   * @param pAttributes          additional attributes describing this change.
   * @return the created IProperty.
   */
  @NotNull
  <E extends T> IProperty<S, E> addProperty(@NotNull IPropertyDescription<S, E> pPropertyDescription,
                                            @Nullable Object... pAttributes);

  /**
   * Adds a new IProperty to this IMutablePropertyPit.
   *
   * @param pType        the type for the new IProperty.
   * @param pName        the name for the new IProperty.
   * @param pAnnotations optional Annotations present at the new IProperty.
   * @param <E>          the created IProperty's type.
   * @return the created IProperty.
   */
  @NotNull
  <E extends T> IProperty<S, E> addProperty(@NotNull Class<E> pType, @NotNull String pName,
                                            @Nullable Annotation... pAnnotations);

  /**
   * Adds a new IProperty to this IMutablePropertyPit.
   *
   * @param pType        the type for the new IProperty.
   * @param pName        the name for the new IProperty.
   * @param pAnnotations optional Annotations present at the new IProperty.
   * @param <E>          the created IProperty's type.
   * @return the created IProperty.
   */
  @NotNull
  <E extends T> IProperty<S, E> addProperty(@NotNull Class<E> pType, @NotNull String pName,
                                            @Nullable Iterable<? extends Annotation> pAnnotations);

  /**
   * Removes a dynamic IProperty. Static IProperty objects can't be removed.
   *
   * @param pPropertyDescription describes the IProperty that shall be removed.
   * @param pAttributes          additional attributes describing this change.
   * @return <tt>true</tt> when an IProperty was removed <tt>false</tt> otherwise.
   */
  boolean removeProperty(@NotNull IPropertyDescription<? super S, ? extends T> pPropertyDescription,
                         @Nullable Object... pAttributes);

  /**
   * Removes a dynamic IProperty. Static IProperty objects can't be removed.
   *
   * @param pProperty the IProperty to be removed.
   * @return <tt>true</tt> when an IProperty was removed <tt>false</tt> otherwise.
   */
  boolean removeProperty(@NotNull IProperty<? super S, ? extends T> pProperty);
}
