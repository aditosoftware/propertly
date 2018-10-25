package de.adito.propertly.core.spi;

import de.adito.propertly.core.common.exception.PropertlyRenameException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * IProperty is the access point for a value including its metadata. That means besides getting and setting its value
 * one can get the type, name, parent and the IPropertyDescription object. Further one can add and remove listeners and
 * in case it's a dynamic IProperty one can rename it.
 *
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:42
 */
public interface IProperty<S extends IPropertyPitProvider, T>
{

  /**
   * @return the IPropertyDescription object for this IProperty.
   */
  @NotNull
  IPropertyDescription<? super S, T> getDescription();

  /**
   * @return the corresponding hierarchy for this property.
   */
  IHierarchy<?> getHierarchy();

  /**
   * @return the current IProperty object's value.
   */
  @Nullable
  T getValue();

  /**
   * This method returns the value which is actually set for this property after this method succeeded. There a several
   * situations where this might occur. So after setting a value one shall proceed with the returned value.
   *
   * @param pValue      the value to be set.
   * @param pAttributes additional attributes describing this change.
   * @return the actual value which was set.
   */
  @Nullable
  T setValue(@Nullable T pValue, @Nullable Object... pAttributes);

  /**
   * @return whether {@link #getValue()} can be used.
   */
  boolean canRead();

  /**
   * @return whether {@link #setValue(Object, Object...)} can be used.
   */
  boolean canWrite();

  /**
   * Checks whether this IProperty is valid.
   *
   * @return whether this IProperty is a proper member of an IHierarchy and is valid thereby.
   */
  boolean isValid();

  /**
   * @return the parental IPropertyPitProvider object this IProperty belongs to. May only be null if this IProperty
   * object holds the root IPropertyPitProvider.
   */
  @Nullable
  S getParent();

  /**
   * Like with variables this returns the class this property's value must be or must inherit from.
   *
   * @return the type for the value.
   */
  @NotNull
  Class<T> getType();

  /**
   * @return this property's name.
   */
  @NotNull
  String getName();

  /**
   * Renaming is only supported when the corresponding IPropertyPitProvider's IPropertyPit is mutable and this IProperty
   * is dynamic.
   *
   * @param pName       the new name for this IProperty.
   * @param pAttributes additional attributes describing this change.
   * @throws PropertlyRenameException in case renaming is not possible. This happens when this property is static, when
   *                                  the name is already in use at the corresponding IPropertyPitProvider or when other
   *                                  custom constraints match.
   */
  void rename(@NotNull String pName, @Nullable Object... pAttributes) throws PropertlyRenameException;

  /**
   * @return whether this Property is defined at runtime and can be renamed or removed.
   */
  boolean isDynamic();

  /**
   * Adds a weak listener.
   *
   * @param pListener the listener to be weakly added.
   */
  void addWeakListener(@NotNull IPropertyEventListener<S, T> pListener);

  /**
   * Adds a strong listener.
   *
   * @param pListener the listener to be strongly added.
   */
  void addStrongListener(@NotNull IPropertyEventListener<S, T> pListener);

  /**
   * Removes a listener.
   *
   * @param pListener the listener to be removed.
   */
  void removeListener(@NotNull IPropertyEventListener<S, T> pListener);

}
