package de.verpalnt.propertly.core;

/**
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:42
 */
public interface IProperty<S, T>
{

  IPropertyDescription<S, T> getDescription();

  T getValue();

  T setValue(T pValue);

}
