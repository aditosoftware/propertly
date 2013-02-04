package de.verpalnt.propertly.core.api;

/**
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:42
 */
public interface IProperty<S, T> extends IPropertyDescription<S, T>
{

  IPropertyDescription<S, T> getDescription();

  T getValue();

  T setValue(T pValue);

}
