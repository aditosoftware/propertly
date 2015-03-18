package de.adito.propertly.core.spi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * IPropertyPit is the main access point for IProperty objects.
 *
 * @author PaL, 09.11.13
 */
public interface IPropertyPit<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    extends IPropertyPitProvider<P, S, T>, Iterable<IProperty<S, T>>
{

  /**
   * @return the IPropertyPitProvider that provides this IPropertyPit.
   */
  @Nonnull
  S getSource();

  /**
   * @return whether this IPropertyPit is valid. A pit is valid when it is connected to a Hierarchy object.
   */
  boolean isValid();

  /**
   * @return the connected Hierarchy object.
   */
  @Nonnull
  IHierarchy<?> getHierarchy();

  /**
   * @return the parental IPropertyPitProvider where this IPropertyPit's IPropertyPitProvider is provided through a
   * IProperty.
   */
  @Nullable
  P getParent();

  /**
   * @return the IProperty that provides this IPropertyPit's IPropertyPitProvider.
   */
  @Nonnull
  IProperty<P, S> getOwnProperty();

  /**
   * Finds an IProperty at this IPropertyPit. In contrast to #getProperty this method can return <tt>null</tt> because
   * the searched description's source does not necessarily have to fit this IPropertyPit's IPropertyPitProvider.
   *
   * @param pPropertyDescription an arbitrary IPropertyDescription that describes the searched IProperty.
   * @param <E>                  the searched IProperty's value type.
   * @return the IProperty if available otherwise <tt>null</tt>.
   */
  @Nullable
  <E> IProperty<S, E> findProperty(@Nonnull IPropertyDescription<?, E> pPropertyDescription);

  /**
   * Returns an IProperty for the supplied IPropertyDescription. In case the searched IPropertyDescription does not
   * exist at this IPropertyPit a RuntimeException is thrown.
   *
   * @param pPropertyDescription an IPropertyDescription from this IPropertyPit's IPropertyPitProvider or one of it's
   *                             super classes or interfaces.
   * @param <E>                  the searched IPropertyPit's value type.
   * @return the IProperty from this IPropertyPit's IPropertyPitProvider.
   */
  @Nonnull
  <E extends T> IProperty<S, E> getProperty(@Nonnull IPropertyDescription<? super S, E> pPropertyDescription);

  /**
   * @param pPropertyDescription an IPropertyDescription from this IPropertyPit's IPropertyPitProvider or one of it's
   *                             super classes or interfaces.
   * @param <E>                  the value's type.
   * @return a value from an IProperty.
   */
  @Nullable
  <E extends T> E getValue(@Nonnull IPropertyDescription<? super S, E> pPropertyDescription);

  /**
   * @param pPropertyDescription an IPropertyDescription from this IPropertyPit's IPropertyPitProvider or one of it's
   *                             super classes or interfaces.
   * @param pValue               the value that shall be set.
   * @param <E>                  the value's type.
   * @return the actual value that is set.
   */
  @Nullable
  <E extends T> E setValue(@Nonnull IPropertyDescription<? super S, E> pPropertyDescription, @Nullable E pValue);

  /**
   * @return all IPropertyDescription objects available at this IPropertyPit.
   */
  @Nonnull
  Set<IPropertyDescription<S, T>> getPropertyDescriptions();

  /**
   * @return all IProperty objects available at this IPropertyPit.
   */
  @Nonnull
  List<IProperty<S, T>> getProperties();

  /**
   * @return the values from all IProperty objects available at this IPropertyPit.
   */
  @Nonnull
  List<T> getValues();

  /**
   * @return the type all children must be inferred from.
   */
  Class<T> getChildType();

  /**
   * Adds a weak listener.
   *
   * @param pListener the listener to be weakly added.
   */
  void addWeakListener(@Nonnull IPropertyPitEventListener pListener);

  /**
   * Adds a strong listener.
   *
   * @param pListener the listener to be strongly added.
   */
  void addStrongListener(@Nonnull IPropertyPitEventListener pListener);

  /**
   * Removes a listener.
   *
   * @param pListener the listener to be removed.
   */
  void removeListener(@Nonnull IPropertyPitEventListener pListener);

}
