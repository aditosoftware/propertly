package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.*;
import de.verpalnt.propertly.core.api.base.*;

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
  public <S extends IIndexedMutablePropertyPitProvider, T> IIndexedMutablePropertyPit<S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType)
  {
    return IndexedMutablePropertyPit.create(pPropertyPitProvider, pAllowedChildType);
  }

  @Override
  public <S extends IMutablePropertyPitProvider, T> IMutablePropertyPit<S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType)
  {
    return MutablePropertyPit.create(pPropertyPitProvider, pAllowedChildType);
  }

  @Override
  public <S extends IPropertyPitProvider> IPropertyPit<S, Object> create(S pPropertyPitProvider)
  {
    return PropertyPit.create(pPropertyPitProvider);
  }

}
