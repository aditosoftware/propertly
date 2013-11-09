package de.verpalnt.propertly.core.hierarchy;

import de.verpalnt.propertly.core.api.IMutablePropertyPit;
import de.verpalnt.propertly.core.api.IMutablePropertyPitProvider;
import de.verpalnt.propertly.core.api.IPropertyPit;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;
import de.verpalnt.propertly.core.api.base.IMutablePropertyPitFactory;
import de.verpalnt.propertly.core.api.base.IPropertyPitFactory;

/**
 * Created by PaL on 09.11.13.
 */
public class HierarchyPitFactory implements IPropertyPitFactory, IMutablePropertyPitFactory
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
  public <S extends IMutablePropertyPitProvider, T> IMutablePropertyPit<S, T> create(S pPropertyPitProvider, Class<T> pAllowedChildType)
  {
    return MutablePropertyPit.create(pPropertyPitProvider, pAllowedChildType);
  }

  @Override
  public <S extends IPropertyPitProvider> IPropertyPit<S> create(S pPropertyPitProvider)
  {
    return PropertyPit.create(pPropertyPitProvider);
  }

}
