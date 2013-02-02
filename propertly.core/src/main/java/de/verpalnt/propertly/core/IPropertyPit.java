package de.verpalnt.propertly.core;

import de.verpalnt.propertly.core.hierarchy.Node;
import de.verpalnt.propertly.core.listener.IPropertyEventListener;

import java.util.List;

/**
 * An IPropertyPit gives access to a set of Properties and supplies listener for registering changes to them.
 *
 * @author PaL
 *         Date: 14.10.12
 *         Time: 15:52
 */
public interface IPropertyPit<S extends IPropertyPitProvider> extends IPropertyPitProvider<S>, IPropertyPitDescription
{
  IPropertyPitProvider getParent();

  <SOURCE, T> IProperty<SOURCE, T> findProperty(IPropertyDescription<SOURCE, T> pPropertyDescription);

  <SOURCE, T> IProperty<SOURCE, T> getProperty(IPropertyDescription<SOURCE, T> pPropertyDescription);

  <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription);

  <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue);

  List<IProperty> getProperties();

  void addPropertyEventListener(IPropertyEventListener pListener);

  void removePropertyEventListener(IPropertyEventListener pListener);

  void setNode(Node pNode);

  Node getNode();

}
