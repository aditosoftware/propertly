package de.adito.propertly.core.common.serialization;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Map with extra information for deserialization to IPropertyPitProviders.
 *
 * @author j.boesl, 27.02.15
 */
public class MapSerializationProviderMap extends LinkedHashMap<String, MapSerializationProviderMap.Entry>
{

  public static class Entry
  {
    Class type;
    Object value;
    List<? extends Annotation> annotations;

    public Entry(Class pType, Object pValue, List<? extends Annotation> pAnnotations)
    {
      type = pType;
      value = pValue;
      annotations = pAnnotations;
    }

    public Class getType()
    {
      return type;
    }

    public Object getValue()
    {
      return value;
    }

    public List<? extends Annotation> getAnnotations()
    {
      return annotations;
    }
  }

}
