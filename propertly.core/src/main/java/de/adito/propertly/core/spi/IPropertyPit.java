package de.adito.propertly.core.spi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  @NotNull
  S getSource();

  /**
   * @return whether this IPropertyPit is valid. A pit is valid when it is connected to a Hierarchy object.
   */
  boolean isValid();

  /**
   * @return the connected Hierarchy object.
   */
  @NotNull
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
  @NotNull
  IProperty<P, S> getOwnProperty();

  /**
   * Finds an IProperty at this IPropertyPit.
   * This method will ignore the case. If the name matches ignoring the case, then a property will be returned.
   *
   * @param pName the name of the searched IProperty.
   * @return the IProperty if available otherwise <tt>null</tt>.
   */
  @Nullable
  IProperty<S, T> findProperty(@NotNull String pName);

  /**
   * Finds an IProperty at this IPropertyPit.
   * This method will strictly check the case. If the case does not match, it will find no results.
   *
   * @param pName the name of the searched IProperty.
   * @return the IProperty if available otherwise <tt>null</tt>.
   */
  @Nullable
  IProperty<S, T> findPropertyWithCase(@NotNull String pName);

  /**
   * Finds an IProperty at this IPropertyPit. In contrast to #getProperty this method can return <tt>null</tt> because
   * the searched description's source does not necessarily have to fit this IPropertyPit's IPropertyPitProvider.
   *
   * @param pPropertyDescription an arbitrary IPropertyDescription that describes the searched IProperty.
   * @param <E>                  the searched IProperty's value type.
   * @return the IProperty if available otherwise <tt>null</tt>.
   */
  @Nullable
  <E> IProperty<S, E> findProperty(@NotNull IPropertyDescription<?, E> pPropertyDescription);

  /**
   * Returns an IProperty for the supplied IPropertyDescription. In case the searched IPropertyDescription does not
   * exist at this IPropertyPit a RuntimeException is thrown.
   *
   * @param pPropertyDescription an IPropertyDescription from this IPropertyPit's IPropertyPitProvider or one of it's
   *                             super classes or interfaces.
   * @param <E>                  the searched IPropertyPit's value type.
   * @return the IProperty from this IPropertyPit's IPropertyPitProvider.
   */
  @NotNull
  <E extends T> IProperty<S, E> getProperty(@NotNull IPropertyDescription<? super S, E> pPropertyDescription);

  /**
   * @param pPropertyDescription an IPropertyDescription from this IPropertyPit's IPropertyPitProvider or one of it's
   *                             super classes or interfaces.
   * @param <E>                  the value's type.
   * @return a value from an IProperty.
   */
  @Nullable
  <E extends T> E getValue(@NotNull IPropertyDescription<? super S, E> pPropertyDescription);

  /**
   * @param pPropertyDescription an IPropertyDescription from this IPropertyPit's IPropertyPitProvider or one of it's
   *                             super classes or interfaces.
   * @param pValue               the value that shall be set.
   * @param <E>                  the value's type.
   * @return the actual value that is set.
   */
  @Nullable
  <E extends T> E setValue(@NotNull IPropertyDescription<? super S, E> pPropertyDescription, @Nullable E pValue);

  /**
   * @return all IPropertyDescription objects available at this IPropertyPit.
   */
  @NotNull
  Set<IPropertyDescription<S, T>> getPropertyDescriptions();

  /**
   * @return all IProperty objects available at this IPropertyPit.
   */
  @NotNull
  List<IProperty<S, T>> getProperties();

  /**
   * @return the values from all IProperty objects available at this IPropertyPit.
   */
  @NotNull
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
  void addWeakListener(@NotNull IPropertyPitEventListener<S, T> pListener);

  /**
   * Adds a strong listener.
   *
   * @param pListener the listener to be strongly added.
   */
  void addStrongListener(@NotNull IPropertyPitEventListener<S, T> pListener);

  /**
   * Removes a listener.
   *
   * @param pListener the listener to be removed.
   */
  void removeListener(@NotNull IPropertyPitEventListener<S, T> pListener);

}
