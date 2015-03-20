package de.adito.propertly.serialization;

import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.*;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * A ISerializationProvider implementation that serializes IPropertyPitProviders to a Map.
 *
 * @author j.boesl, 27.02.15
 */
public class MapSerializationProvider implements ISerializationProvider<Map<String, Object>, Map<String, Object>>
{

  @Nonnull
  @Override
  public Map<String, Object> serializeRootNode(
      @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
      @Nonnull IChildRunner<Map<String, Object>> pChildRunner)
  {
    LinkedHashMap<String, Object> root = new LinkedHashMap<String, Object>();
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    root.put(pName, new Property(pPropertyType, null, map, null));
    pChildRunner.run(map);
    return root;
  }

  @Override
  public void serializeFixedNode(
      @Nonnull Map<String, Object> pParentOutputData, @Nonnull String pName,
      @Nonnull IChildRunner<Map<String, Object>> pChildRunner)
  {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    pParentOutputData.put(pName, map);
    pChildRunner.run(map);
  }

  @Override
  public void serializeFixedNode(
      @Nonnull Map<String, Object> pParentOutputData, @Nonnull String pName,
      @Nonnull Class<? extends IPropertyPitProvider> pType, @Nonnull IChildRunner<Map<String, Object>> pChildRunner)
  {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    pParentOutputData.put(pName, new Property(null, pType, map, null));
    pChildRunner.run(map);
  }

  @Override
  public void serializeDynamicNode(
      @Nonnull Map<String, Object> pParentOutputData, @Nonnull String pName,
      @Nonnull Class<? extends IPropertyPitProvider> pPropertyType, @Nullable List<? extends Annotation> pAnnotations,
      @Nonnull IChildRunner<Map<String, Object>> pChildRunner)
  {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    pParentOutputData.put(pName, new Property(pPropertyType, null, map, pAnnotations));
    pChildRunner.run(map);
  }

  @Override
  public void serializeDynamicNode(
      @Nonnull Map<String, Object> pParentOutputData, @Nonnull String pName,
      @Nonnull Class<? extends IPropertyPitProvider> pPropertyType, @Nonnull Class<? extends IPropertyPitProvider> pType,
      @Nullable List<? extends Annotation> pAnnotations, @Nonnull IChildRunner<Map<String, Object>> pChildRunner)
  {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    pParentOutputData.put(pName, new Property(pPropertyType, pType, map, pAnnotations));
    pChildRunner.run(map);
  }

  @Override
  public void serializeFixedValue(
      @Nonnull Map<String, Object> pParentOutputData, @Nonnull String pName, @Nonnull Object pValue)
  {
    pParentOutputData.put(pName, pValue);
  }

  @Override
  public <V> void serializeDynamicValue(
      @Nonnull Map<String, Object> pParentOutputData, @Nonnull String pName, @Nonnull Class<? super V> pPropertyType,
      @Nullable V pValue, @Nullable List<? extends Annotation> pAnnotations)
  {
    pParentOutputData.put(pName, new Property(pPropertyType, pValue, null, pAnnotations));
  }


  @Override
  public void deserializeRoot(@Nonnull Map<String, Object> pInputData, @Nonnull IChildAppender<Map<String, Object>> pChildAppender)
  {
    deserializeChild(pInputData, pChildAppender);
  }

  @Override
  public void deserializeChild(
      @Nonnull Map<String, Object> pInputData, @Nonnull IChildAppender<Map<String, Object>> pChildAppender)
  {
    for (Map.Entry<String, Object> mapEntry : pInputData.entrySet())
    {
      String name = mapEntry.getKey();
      Object mapValue = mapEntry.getValue();
      IChildDetail childDetail = pChildAppender.getChildDetail(name);

      switch (childDetail.getCategory())
      {
        case FIXED_NODE:
          if (mapValue instanceof Map)
            //noinspection unchecked
            pChildAppender.appendFixedNode((Map<String, Object>) mapValue, name);
          else
          {
            Property prop = (Property) mapValue;
            //noinspection unchecked
            pChildAppender.appendFixedNode(prop.getChildren(), name, (Class) prop.getValue());
          }
          break;
        case FIXED_VALUE:
          pChildAppender.appendFixedValue(name, mapValue);
          break;
        case DYNAMIC:
        default:
          Property prop = (Property) mapValue;
          Class type = prop.getType();
          Object value = prop.getValue();
          Map<String, Object> children = prop.getChildren();
          List<? extends Annotation> annotations = prop.getAnnotations();
          if (IPropertyPitProvider.class.isAssignableFrom(type))
          {
            if (prop.getValue() == null)
              //noinspection unchecked
              pChildAppender.appendDynamicNode(children, name, type, annotations);
            else
              //noinspection unchecked
              pChildAppender.appendDynamicNode(children, name, type, (Class) value, annotations);
          }
          else
            pChildAppender.appendDynamicValue(name, type, value, annotations);
          break;
      }
    }
  }


  /**
   * Value class for properties.
   */
  public static class Property
  {
    Class type;
    Object value;
    private Map<String, Object> children;
    List<? extends Annotation> annotations;

    public Property(Class pType, Object pValue, Map<String, Object> pChildren, List<? extends Annotation> pAnnotations)
    {
      type = pType;
      value = pValue;
      children = pChildren;
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

    public Map<String, Object> getChildren()
    {
      return children;
    }

    public List<? extends Annotation> getAnnotations()
    {
      return annotations;
    }
  }
}
