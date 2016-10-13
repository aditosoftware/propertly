package de.adito.propertly.diff.data;

import de.adito.propertly.core.common.path.IPropertyPath;
import de.adito.propertly.core.spi.IProperty;

import java.util.List;

/**
 * @author j.boesl, 19.07.2015
 */
public interface IPropertlyNode<T, S extends IPropertlyNode<T, S>>
{

  int getId();

  int getRating(S pMatch);

  T getRef();

  String getName();

  S getParent();

  List<S> getChildren();

  void addChild(S pNode);

  void addChild(int pIndex, S pNode, String pNewName);

  void remove();

  S getChild(String pName);

  IPropertyPath getPath();

  S resolveChild(IPropertyPath pPath);

  S createCopy();

}
