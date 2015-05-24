package de.adito.propertly.serialization.structuredescription;

import de.adito.propertly.core.common.PPPIntrospector;
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
        new LinkedHashMap<Class<? extends IPropertyPitProvider>, IPPPDescription>();
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
          if (IMutablePropertyPitProvider.class.isAssignableFrom(description.getType()))
          {
            //noinspection unchecked
            Class<?> childType = PPPIntrospector.getChildType(description.getType());
            if (IPropertyPitProvider.class.isAssignableFrom(childType))
              //noinspection unchecked
              subClasses.add((Class<? extends IPropertyPitProvider>) childType);
          }
        }
      }
    }
    if (!subClasses.isEmpty())
      _collect(pMap, subClasses);
  }

  private static class _PPPDescription implements IPPPDescription
  {
    private Class<? extends IPropertyPitProvider> type;

    public _PPPDescription(Class<? extends IPropertyPitProvider> pType)
    {
      type = pType;
    }

    @Override
    public Class<? extends IPropertyPitProvider> getType()
    {
      return type;
    }

    @Override
    public Class getAllowedSubType()
    {
      return PPPIntrospector.getChildType(type);
    }

    @Override
    public boolean isMutable()
    {
      return IMutablePropertyPitProvider.class.isAssignableFrom(type);
    }

    @Override
    public Set<IPropertyDescription> getPropertyDescriptions()
    {
      return PPPIntrospector.get(type);
    }
  }

}
