package de.verpalnt.propertly.core.common;

import de.verpalnt.propertly.core.api.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:48
 */
public class PPPIntrospector
{

  private static final Map<Class, Set<IPropertyDescription>> ALREADY_KNOWN = new LinkedHashMap<Class, Set<IPropertyDescription>>();

  private PPPIntrospector()
  {
  }

  public static Set<IPropertyDescription> get(Class<? extends IPropertyPitProvider> pPPPClass)
  {
    Set<IPropertyDescription> propertyDescriptions = ALREADY_KNOWN.get(pPPPClass);
    if (propertyDescriptions == null)
    {
      propertyDescriptions = new LinkedHashSet<IPropertyDescription>();
      for (Field field : _getAllFields(pPPPClass))
      {
        try
        {
          field.setAccessible(true);
          if (IPropertyDescription.class.isAssignableFrom(field.getType()))
          {
            IPropertyDescription<?, ?> propertyDescription = (IPropertyDescription) field.get(pPPPClass);
            boolean isParentalType = propertyDescription.getSourceType().isAssignableFrom(pPPPClass);
            if (isParentalType)
              propertyDescriptions.add(propertyDescription);
            assert isParentalType;
          }
        }
        catch (IllegalAccessException e)
        {
          // skip
        }
      }
      propertyDescriptions = Collections.unmodifiableSet(propertyDescriptions);
      ALREADY_KNOWN.put(pPPPClass, propertyDescriptions);
    }
    return propertyDescriptions;
  }

  private static List<Field> _getAllFields(Class pCls)
  {
    return _addFields(new ArrayList<Field>(), new HashSet<Class>(), pCls);
  }

  private static List<Field> _addFields(List<Field> pResult, Set<Class> pProcessed, Class pCls)
  {
    if (pProcessed.add(pCls))
    {
      Collections.addAll(pResult, pCls.getDeclaredFields());
      for (Class<?> c = pCls; c != null && c != Object.class; c = c.getSuperclass())
        _addFields(pResult, pProcessed, c);
      for (Class<?> face : pCls.getInterfaces())
        _addFields(pResult, pProcessed, face);
    }
    return pResult;
  }

}
