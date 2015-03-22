package de.adito.propertly.serialization.converter.impl;

import de.adito.propertly.serialization.converter.*;
import net.java.sezpoz.*;

import javax.annotation.*;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.logging.*;

/**
 * @author j.boesl, 11.03.15
 */
public abstract class AbstractSubTypeStringConverter<T> implements IObjectConverter<T>
{

  private Map<Class<? extends T>, String> map = new HashMap<Class<? extends T>, String>();
  private Map<String, Class<? extends T>> reverseMap = new HashMap<String, Class<? extends T>>();


  public AbstractSubTypeStringConverter()
  {
    Index<ConverterSubTypeProvider, T> index = Index.load(ConverterSubTypeProvider.class, getCommonType());
    for (IndexItem<ConverterSubTypeProvider, T> indexItem : index)
    {
      if (indexItem.kind() == ElementType.TYPE)
      {
        try
        {
          AnnotatedElement element = indexItem.element();
          //noinspection unchecked
          register((Class<? extends T>) element, indexItem.annotation().value());
        }
        catch (InstantiationException e)
        {
          Logger.getLogger(getClass().getCanonicalName()).
              log(Level.WARNING, "Annotated wrong element with " + ConverterSubTypeProvider.class.getSimpleName(), e);
        }
      }
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
    try
    {
      Class<? extends T> cls = reverseMap.get(pTypeAsString);
      //noinspection unchecked
      return cls == null ? (Class<? extends T>) Class.forName(pTypeAsString) : cls;
    }
    catch (ClassNotFoundException e)
    {
      return null;
    }
  }

  @Nonnull
  @Override
  public String typeToString(@Nonnull Class<? extends T> pType)
  {
    String s = map.get(pType);
    return s == null ? pType.getCanonicalName() : s;
  }

}
