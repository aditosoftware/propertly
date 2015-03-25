package de.adito.propertly.serialization.converter.impl;

import de.adito.picoservice.IPicoRegistry;
import de.adito.propertly.serialization.converter.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author j.boesl, 11.03.15
 */
public abstract class AbstractSubTypeStringConverter<T> implements IObjectConverter<T>
{

  private Map<Class<? extends T>, String> map = new HashMap<Class<? extends T>, String>();
  private Map<String, Class<? extends T>> reverseMap = new HashMap<String, Class<? extends T>>();


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

  public void register(@Nonnull Class<? extends T> pCls, @Nullable String pRepresentation)
  {
    if (pRepresentation == null)
      pRepresentation = pCls.getSimpleName();
    map.put(pCls, pRepresentation);
    reverseMap.put(pRepresentation, pCls);
  }

  @Nullable
  @Override
  public Class<? extends T> stringToType(@Nonnull String pTypeAsString)
  {
    return reverseMap.get(pTypeAsString);
  }

  @Nonnull
  @Override
  public String typeToString(@Nonnull Class<? extends T> pType)
  {
    String s = map.get(pType);
    if (s == null)
      throw new RuntimeException("No converter found for '" + pType + "'.");
    return s;
  }

}
