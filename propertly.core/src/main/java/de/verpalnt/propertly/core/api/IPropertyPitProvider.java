package de.verpalnt.propertly.core.api;

/**
 * @author PaL
 *         Date: 14.10.12
 *         Time: 14:52
 */
public interface IPropertyPitProvider<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
{

  IPropertyPit<P, S, T> getPit();

}
