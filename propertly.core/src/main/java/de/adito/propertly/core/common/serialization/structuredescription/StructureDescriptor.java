package de.adito.propertly.core.common.serialization.structuredescription;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;

import java.util.*;

/**
 * @author j.boesl, 09.03.15
 */
public class StructureDescriptor
{

  private IDescriptionProvider descriptionProvider;

  public StructureDescriptor(IDescriptionProvider pDescriptionProvider)
  {
    descriptionProvider = pDescriptionProvider;
  }

  public String getStructureDescription(Iterable<Class<? extends IPropertyPitProvider>> pPPPClasses)
  {
    Map<Class<? extends IPropertyPitProvider>, IPPPDescription> map =
        new HashMap<Class<? extends IPropertyPitProvider>, IPPPDescription>();
    _collect(map, pPPPClasses);
    return descriptionProvider.getSerializationDescription(map);
  }

  private void _collect(Map<Class<? extends IPropertyPitProvider>, IPPPDescription> pMap,
                        Iterable<Class<? extends IPropertyPitProvider>> pPPPClasses)
  {
    Set<Class<? extends IPropertyPitProvider>> subClasses = new LinkedHashSet<Class<? extends IPropertyPitProvider>>();
    for (Class<? extends IPropertyPitProvider> cls : pPPPClasses)
    {
      if (!pMap.containsKey(cls))
      {
        _PPPDescription pppDescription = new _PPPDescription(cls);
        pMap.put(cls, pppDescription);
        for (IPropertyDescription description : pppDescription.getPropertyDescriptions())
        {
          if (IPropertyPitProvider.class.isAssignableFrom(description.getType()))
            //noinspection unchecked
            subClasses.add(description.getType());
        }
      }
    }
    _collect(pMap, subClasses);
  }

  private class _PPPDescription implements IPPPDescription
  {
    private IPropertyPitProvider ppp;

    public _PPPDescription(Class<? extends IPropertyPitProvider> pType)
    {
      ppp = PropertlyUtility.create(pType);
    }

    @Override
    public Class<? extends IPropertyPitProvider> getType()
    {
      return ppp.getClass();
    }

    @Override
    public Class getAllowedSubType()
    {
      return isMutable() ? ((IMutablePropertyPitProvider) ppp).getPit().getChildType() : Object.class;
    }

    @Override
    public boolean isMutable()
    {
      return ppp instanceof IMutablePropertyPitProvider;
    }

    @Override
    public Set<IPropertyDescription> getPropertyDescriptions()
    {
      //noinspection unchecked
      return ppp.getPit().getPropertyDescriptions();
    }
  }

}
