package de.verpalnt.propertly.core.api;

import de.verpalnt.propertly.core.hierarchy.PropertlyRenameException;

import javax.annotation.Nonnull;

/**
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:42
 */
public interface IProperty<S extends IPropertyPitProvider, T>
{

  IPropertyDescription<? super S, T> getDescription();

  T getValue();

  T setValue(T pValue);

  S getParent();

  Class<T> getType();

  String getName();

  void rename(@Nonnull String pName) throws PropertlyRenameException;

  void addPropertyEventListener(IPropertyEventListener pListener);

  void removePropertyEventListener(IPropertyEventListener pListener);

}
