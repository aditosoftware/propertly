package de.adito.propertly.core.common;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.hierarchy.PropertyDescription;

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
      LinkedHashMap<String, List<IPropertyDescription>> namePropertyMap = new LinkedHashMap<String, List<IPropertyDescription>>();
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
            {
              List<IPropertyDescription> list = namePropertyMap.get(propertyDescription.getName());
              if (list == null)
              {
                list = new ArrayList<IPropertyDescription>();
                namePropertyMap.put(propertyDescription.getName(), list);
              }
              list.add(propertyDescription);
            }
            assert isParentalType;
          }
        }
        catch (IllegalAccessException e)
        {
          // skip
        }
      }
      propertyDescriptions = new LinkedHashSet<IPropertyDescription>();
      for (List<IPropertyDescription> descriptions : namePropertyMap.values())
      {
        IPropertyDescription firstDescription = descriptions.get(0);
        if (descriptions.size() == 1)
          propertyDescriptions.add(firstDescription);
        else
        {
          Class type = firstDescription.getType();
          for (IPropertyDescription description : descriptions)
            if (!type.equals(description.getType()))
              throw new IllegalStateException("Incompatible descriptions are used together: " + descriptions);
          //noinspection unchecked
          propertyDescriptions.add(PropertyDescription.create(
              pPPPClass, type, firstDescription.getName(), firstDescription.getAnnotations()));
        }
      }
      propertyDescriptions = Collections.unmodifiableSet(propertyDescriptions);
      ALREADY_KNOWN.put(pPPPClass, propertyDescriptions);
    }
    return propertyDescriptions;
  }

  private static List<Field> _getAllFields(Class pCls)
  {
    ArrayList<Field> result = new ArrayList<Field>();
    HashSet<Class> processed = new HashSet<Class>();
    processed.add(Object.class); // shall not be processed.
    _addFields(result, processed, Arrays.asList(pCls));
    return result;
  }

  private static void _addFields(List<Field> pResult, Set<Class> pProcessed, List<Class> pClasses)
  {
    if (pClasses == null || pClasses.isEmpty())
      return;

    List<Class> superClasses = new ArrayList<Class>();
    for (Class cls : pClasses)
    {
      if (_addCurrentFields(pResult, pProcessed, cls))
      {
        Class superclass = cls.getSuperclass();
        if (superclass != null)
          superClasses.add(superclass);
        superClasses.addAll(Arrays.asList(cls.getInterfaces()));
      }
    }
    _addFields(pResult, pProcessed, superClasses);
  }

  private static boolean _addCurrentFields(List<Field> pResult, Set<Class> pProcessed, Class pCls)
  {
    boolean added = pProcessed.add(pCls);
    if (added)
      Collections.addAll(pResult, pCls.getDeclaredFields());
    return added;
  }

}
