package de.adito.propertly.core.common.serialization;

import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.*;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * A ISerializationProvider implementation that serializes IPropertyPitProviders to a Map.
 *
 * @author j.boesl, 27.02.15
 */
public class MapSerializationProvider implements ISerializationProvider<MapSerializationProviderMap>
{

  @Override
  public <V> void serializeValue(
      @Nonnull MapSerializationProviderMap pParentOutputData, @Nonnull Class<V> pType, @Nonnull String pName,
      @Nullable List<? extends Annotation> pAnnotations, @Nullable V pValue)
  {
    pParentOutputData.put(pName, new MapSerializationProviderMap.Entry(pType, pValue, pAnnotations));
  }

  @Nonnull
  @Override
  public MapSerializationProviderMap serializeNode(
      @Nullable MapSerializationProviderMap pParentOutputData, @Nonnull Class<? extends IPropertyPitProvider> pType,
      @Nonnull String pName, @Nullable List<? extends Annotation> pAnnotations, @Nullable IPropertyPitProvider pValue,
      @Nonnull ChildRunner<MapSerializationProviderMap> pChildRunner)
  {
    if (pParentOutputData == null) // root
      pParentOutputData = new MapSerializationProviderMap();

    MapSerializationProviderMap map = pValue == null ? null : new MapSerializationProviderMap();
    pParentOutputData.put(pName, new MapSerializationProviderMap.Entry(pType, map, pAnnotations));

    if (map != null)
      pChildRunner.run(map);

    return pParentOutputData;
  }

  @Override
  public void deserialize(
      @Nonnull MapSerializationProviderMap pInputData, @Nonnull ChildAppender<MapSerializationProviderMap> pAppendChild)
  {
    for (Map.Entry<String, MapSerializationProviderMap.Entry> mapEntry : pInputData.entrySet())
    {
      String name = mapEntry.getKey();
      MapSerializationProviderMap.Entry entry = mapEntry.getValue();

      Class type = entry.getType();
      List<? extends Annotation> annotations = entry.getAnnotations();
      Object value = entry.getValue();

      if (IPropertyPitProvider.class.isAssignableFrom(type))
        //noinspection unchecked
        pAppendChild.appendNode((MapSerializationProviderMap) value, type, name, annotations);
      else
        pAppendChild.appendValue(type, name, annotations, value);
    }
  }

}
