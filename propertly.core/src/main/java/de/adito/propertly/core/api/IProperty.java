package de.adito.propertly.core.api;

import de.adito.propertly.core.hierarchy.PropertlyRenameException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
  @Nonnull
  IPropertyDescription<? super S, T> getDescription();

  /**
   * @return the current IProperty object's value.
   */
  @Nullable
  T getValue();

  /**
   * This method returns the value which is now actually set for this property. There a several situations where this
   * might occur. So after setting a value one shall proceed with the returned value.
   *
   * @param pValue the value to be set.
   * @return the actual value which was set.
   */
  @Nullable
  T setValue(@Nullable T pValue);

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
  @Nonnull
  Class<T> getType();

  /**
   * @return this property's name.
   */
  @Nonnull
  String getName();

  /**
   * Renaming is only supported when the corresponding IPropertyPitProvider's IPropertyPit is mutable.
   *
   * @param pName the new name for this IProperty.
   * @throws PropertlyRenameException in case renaming is not possible. This happens when this property is static, when
   *                                  the name is already in use at the corresponding IPropertyPitProvider or when other
   *                                  custom constraints match.
   */
  void rename(@Nonnull String pName) throws PropertlyRenameException;

  /**
   * Adds a Listener.
   *
   * @param pListener the listener to be added.
   */
  void addPropertyEventListener(@Nonnull IPropertyEventListener pListener);

  /**
   * Removes a listener.
   *
   * @param pListener the listener to be removed.
   */
  void removePropertyEventListener(@Nonnull IPropertyEventListener pListener);

}
