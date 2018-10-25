package de.adito.propertly.serialization.converter.impl;

import de.adito.picoservice.IPicoRegistry;
import de.adito.propertly.serialization.converter.ConverterSubTypeProvider;
import de.adito.propertly.serialization.converter.IObjectConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author j.boesl, 11.03.15
 */
public abstract class AbstractSubTypeStringConverter<T> implements IObjectConverter<T>
{

  private Map<Class<? extends T>, String> map = new HashMap<>();
  private Map<String, Class<? extends T>> reverseMap = new HashMap<>();


  public AbstractSubTypeStringConverter()
  {
    Map<Class<? extends T>, ConverterSubTypeProvider> subConverterMap =
        IPicoRegistry.INSTANCE.find(getCommonType(), ConverterSubTypeProvider.class);
    for (Map.Entry<Class<? extends T>, ConverterSubTypeProvider> entry : subConverterMap.entrySet())
    {
      //noinspection unchecked
      register(entry.getKey(), entry.getValue().value());
    }
  }

  public void register(@NotNull Class<? extends T> pCls, @Nullable String pRepresentation)
  {
    if (pRepresentation == null)
      pRepresentation = pCls.getSimpleName();
    map.put(pCls, pRepresentation);
    reverseMap.put(pRepresentation, pCls);
  }

  @Nullable
  @Override
  public Class<? extends T> stringToType(@NotNull String pTypeAsString)
  {
    return reverseMap.get(pTypeAsString);
  }

  @NotNull
  @Override
  public String typeToString(@NotNull Class<? extends T> pType)
  {
    String s = map.get(pType);
    if (s == null)
      throw new RuntimeException("No converter found for '" + pType + "'.");
    return s;
  }

}
