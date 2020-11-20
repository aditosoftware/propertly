package de.adito.propertly.core.common;

import de.adito.propertly.core.common.annotations.PropertlyOverride;
import de.adito.propertly.core.common.exception.WrongModifiersException;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * PPPIntrospector supplies all IPropertyDescriptions for a class inherited from IPropertyPitProvider.
 *
 * @author PaL
 *         Date: 29.01.13
 *         Time: 23:48
 */
public class PPPIntrospector
{

  private static final Map<Class, Set<IPropertyDescription>> ALREADY_KNOWN = new LinkedHashMap<>();
  private static final Map<Class<? extends IPropertyPitProvider>, Class> CHILD_TYPES = new HashMap<>();

  private PPPIntrospector()
  {
  }

  /**
   * Resolves the child type for an IPropertyPitProvider class.
   *
   * @param pCls an IPropertyPitProvider implementation.
   * @return the child type.
   */
  public static Class<?> getChildType(Class<? extends IPropertyPitProvider> pCls)
  {
    Class<?> childType = CHILD_TYPES.get(pCls);
    if (childType == null)
    {
      if (IPropertyPitProvider.class.isAssignableFrom(pCls))
      {
        try
        {
          IPropertyPitProvider ppp = PropertlyUtility.create(pCls);
          childType = ppp.getPit().getChildType();
        }
        catch (Exception e)
        {
          childType = Object.class;
        }
      }
      else
        throw new IllegalArgumentException("invalid class: " + pCls);

      CHILD_TYPES.put(pCls, childType);
    }
    return childType;
  }

  /**
   * @param pPPPClass the class which gets inspected.
   * @return all IPropertyDescriptions available at given IPropertyPitProvider.
   */
  public static Set<IPropertyDescription> get(Class<? extends IPropertyPitProvider> pPPPClass)
  {
    Set<IPropertyDescription> propertyDescriptions = ALREADY_KNOWN.get(pPPPClass);
    if (propertyDescriptions == null)
    {
      LinkedHashMap<String, List<IPropertyDescription>> namePropertyMap = new LinkedHashMap<>();
      for (Field field : _getAllFields(pPPPClass))
      {
        try
        {
          field.setAccessible(true);
          if (IPropertyDescription.class.isAssignableFrom(field.getType()))
          {
            int modifiers = field.getModifiers();
            if (!(Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)))
              throw new WrongModifiersException("Wrong modifiers for '" + field.getName()
                                                    + "'. IPropertyDescriptions must have static and final modifiers for PPPIntrospection.");
            IPropertyDescription<?, ?> propertyDescription = (IPropertyDescription) field.get(pPPPClass);
            boolean isParentalType = propertyDescription.getSourceType().isAssignableFrom(pPPPClass);
            if (isParentalType)
            {
              List<IPropertyDescription> list = namePropertyMap.get(propertyDescription.getName());
              if (list == null)
              {
                list = new ArrayList<>();
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
      propertyDescriptions = new LinkedHashSet<>();
      for (List<IPropertyDescription> descriptions : namePropertyMap.values())
      {
        IPropertyDescription firstDescription = descriptions.get(0);
        PropertlyOverride propertlyOverride = firstDescription.getAnnotation(PropertlyOverride.class);
        if (descriptions.size() == 1)
        {
          if (propertlyOverride != null)
            throw new IllegalStateException("'" + firstDescription + "' at '" + firstDescription.getSourceType()
                                                + "' is annotated with @PropertlyOverride but does not override.");
        }
        else
        {
          Class type = firstDescription.getType();
          boolean needsOverride = false;
          for (IPropertyDescription<?, ?> description : descriptions)
          {
            if (!type.equals(description.getType()))
              throw new IllegalStateException("Incompatible descriptions are used together: " + descriptions);
            if (!needsOverride && !description.equals(firstDescription) &&
                description.getSourceType().isAssignableFrom(firstDescription.getSourceType()))
              needsOverride = true;
          }
          if (needsOverride && propertlyOverride == null)
            throw new IllegalStateException("'" + firstDescription + "' at '" + firstDescription.getSourceType()
                                                + "' overrides another description an thus has to be annotated with @PropertlyOverride.");
        }
        propertyDescriptions.add(firstDescription);
      }
      propertyDescriptions = Collections.unmodifiableSet(propertyDescriptions);
      ALREADY_KNOWN.put(pPPPClass, propertyDescriptions);
    }
    return propertyDescriptions;
  }

  private static List<Field> _getAllFields(Class pCls)
  {
    ArrayList<Field> result = new ArrayList<>();
    HashSet<Class> processed = new HashSet<>();
    processed.add(Object.class); // shall not be processed.
    _addFields(result, processed, Collections.singletonList(pCls));
    return result;
  }

  private static void _addFields(List<Field> pResult, Set<Class> pProcessed, List<Class> pClasses)
  {
    if (pClasses == null || pClasses.isEmpty())
      return;

    List<Class> superClasses = new ArrayList<>();
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
