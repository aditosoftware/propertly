package de.verpalnt.propertly.core.common;

import de.verpalnt.propertly.core.api.IPropertyDescription;
import de.verpalnt.propertly.core.api.IPropertyPitProvider;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:48
 */
public class PPPIntrospector
{

  private static final Map<Class, Set<IPropertyDescription>> ALREADY_KNOWN = new HashMap<Class, Set<IPropertyDescription>>();

  private PPPIntrospector()
  {
  }

  public static Set<IPropertyDescription> get(Class<? extends IPropertyPitProvider> pPPPClass)
  {
    Set<IPropertyDescription> propertyDescriptions = ALREADY_KNOWN.get(pPPPClass);
    if (propertyDescriptions == null)
    {
      propertyDescriptions = new HashSet<IPropertyDescription>();
      for (Field field : _getAllFields(pPPPClass))
      {
        try
        {
          field.setAccessible(true);
          if (IPropertyDescription.class.isAssignableFrom(field.getType()))
          {
            IPropertyDescription<?, ?> propertyDescription = (IPropertyDescription) field.get(pPPPClass);
            boolean isParentalType = propertyDescription.getParentType().isAssignableFrom(pPPPClass);
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
    List<Field> fields = new ArrayList<Field>();
    Collections.addAll(fields, pCls.getDeclaredFields());
    for (Class<?> c = pCls; c != null && c != Object.class; c = c.getSuperclass())
      fields.addAll(Arrays.asList(c.getDeclaredFields()));
    for (Class<?> face : pCls.getInterfaces())
      Collections.addAll(fields, face.getDeclaredFields());
    return fields;
  }

}
