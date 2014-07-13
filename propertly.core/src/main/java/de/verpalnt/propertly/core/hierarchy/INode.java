package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IProperty;
import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyEventListener;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author PaL
 *         Date: 06.04.13
 *         Time: 18:59
 */
public interface INode
{
  Object setValue(Object pValue);

  Object getValue();

  @Nullable
  List<INode> getChildren();

  void addProperty(IPropertyDescription pPropertyDescription);

  boolean removeProperty(String pName);

  void addProperty(int pIndex, IPropertyDescription pPropertyDescription);

  void removeProperty(int pIndex);

  Hierarchy getHierarchy();

  INode getParent();

  String getPath();

  IProperty getProperty();

  void addPropertyEventListener(IPropertyEventListener pListener);

  void removePropertyEventListener(IPropertyEventListener pListener);
}
