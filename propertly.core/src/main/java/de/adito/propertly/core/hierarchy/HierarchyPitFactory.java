package de.adito.propertly.core.hierarchy;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.api.base.*;

/**
 * @author PaL, 09.11.13
 */
public class HierarchyPitFactory implements IPropertyPitFactory, IMutablePropertyPitFactory, IIndexedMutablePropertyPitFactory
{

  private static final HierarchyPitFactory INSTANCE = new HierarchyPitFactory();

  private HierarchyPitFactory()
  {
  }

  public static HierarchyPitFactory getInstance()
  {
    return INSTANCE;
  }

  @Override
  public <P extends IPropertyPitProvider, S extends IIndexedMutablePropertyPitProvider<P, S, T>, T>
  IIndexedMutablePropertyPit<P, S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType)
  {
    return IndexedMutablePropertyPit.create(pPropertyPitProvider, pAllowedChildType);
  }

  @Override
  public <P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
  IMutablePropertyPit<P, S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType)
  {
    return MutablePropertyPit.create(pPropertyPitProvider, pAllowedChildType);
  }

  @Override
  public <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
  IPropertyPit<P, S, T> create(S pPropertyPitProvider)
  {
    return PropertyPit.create(pPropertyPitProvider);
  }

}
