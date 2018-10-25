package de.adito.propertly.serialization;

import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A ISerializationProvider implementation that serializes IPropertyPitProviders to a Map.
 *
 * @author j.boesl, 27.02.15
 */
public class MapSerializationProvider implements ISerializationProvider<Map<String, Object>, Map<String, Object>>
{

  @NotNull
  @Override
  public Map<String, Object> serializeRootNode(
      @NotNull String pName, @NotNull Class<? extends IPropertyPitProvider> pPropertyType,
      @NotNull IChildRunner<Map<String, Object>> pChildRunner)
  {
    LinkedHashMap<String, Object> root = new LinkedHashMap<>();
    Map<String, Object> map = new LinkedHashMap<>();
    root.put(pName, new Property(pPropertyType, null, map, null));
    pChildRunner.run(map);
    return root;
  }

  @Override
  public void serializeFixedNode(
      @NotNull Map<String, Object> pParentOutputData, @NotNull String pName,
      @NotNull IChildRunner<Map<String, Object>> pChildRunner)
  {
    Map<String, Object> map = new LinkedHashMap<>();
    pParentOutputData.put(pName, map);
    pChildRunner.run(map);
  }

  @Override
  public void serializeFixedNode(
      @NotNull Map<String, Object> pParentOutputData, @NotNull String pName,
      @NotNull Class<? extends IPropertyPitProvider> pType, @NotNull IChildRunner<Map<String, Object>> pChildRunner)
  {
    Map<String, Object> map = new LinkedHashMap<>();
    pParentOutputData.put(pName, new Property(null, pType, map, null));
    pChildRunner.run(map);
  }

  @Override
  public void serializeDynamicNode(
      @NotNull Map<String, Object> pParentOutputData, @NotNull String pName,
      @NotNull Class<? extends IPropertyPitProvider> pPropertyType, @Nullable List<? extends Annotation> pAnnotations,
      @NotNull IChildRunner<Map<String, Object>> pChildRunner)
  {
    Map<String, Object> map = new LinkedHashMap<>();
    pParentOutputData.put(pName, new Property(pPropertyType, null, map, pAnnotations));
    pChildRunner.run(map);
  }

  @Override
  public void serializeDynamicNode(
      @NotNull Map<String, Object> pParentOutputData, @NotNull String pName,
      @NotNull Class<? extends IPropertyPitProvider> pPropertyType, @NotNull Class<? extends IPropertyPitProvider> pType,
      @Nullable List<? extends Annotation> pAnnotations, @NotNull IChildRunner<Map<String, Object>> pChildRunner)
  {
    Map<String, Object> map = new LinkedHashMap<>();
    pParentOutputData.put(pName, new Property(pPropertyType, pType, map, pAnnotations));
    pChildRunner.run(map);
  }

  @Override
  public void serializeFixedValue(
      @NotNull Map<String, Object> pParentOutputData, @NotNull String pName, @NotNull Object pValue)
  {
    pParentOutputData.put(pName, pValue);
  }

  @Override
  public <V> void serializeDynamicValue(
      @NotNull Map<String, Object> pParentOutputData, @NotNull String pName, @NotNull Class<? super V> pPropertyType,
      @Nullable V pValue, @Nullable List<? extends Annotation> pAnnotations)
  {
    pParentOutputData.put(pName, new Property(pPropertyType, pValue, null, pAnnotations));
  }


  @Override
  public void deserializeRoot(@NotNull Map<String, Object> pInputData, @NotNull IChildAppender<Map<String, Object>> pChildAppender)
  {
    deserializeChild(pInputData, pChildAppender);
  }

  @Override
  public void deserializeChild(
      @NotNull Map<String, Object> pInputData, @NotNull IChildAppender<Map<String, Object>> pChildAppender)
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
