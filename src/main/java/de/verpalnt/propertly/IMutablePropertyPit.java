package de.verpalnt.propertly;

/**
 * An IMutablePropertyPit allows to modify the available properties it provides.
 *
 * @author PaL
 *         Date: 18.11.12
 *         Time: 15:44
 */
public interface IMutablePropertyPit<S> extends IPropertyPit<S>
{

  <T> IProperty<? super S, T> addProperty(IPropertyDescription<? super S, T> pPropertyDescription);

  <T> IProperty<? super S, T> removeProperty(IPropertyDescription<? super S, T> pPropertyDescription);

}
