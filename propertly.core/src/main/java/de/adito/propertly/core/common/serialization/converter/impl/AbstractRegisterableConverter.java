package de.adito.propertly.core.common.serialization.converter.impl;

import de.adito.propertly.core.common.serialization.converter.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author j.boesl, 11.03.15
 */
public abstract class AbstractRegisterableConverter<T> implements ITypeStringConverter<T>
{

  private Map<Class<? extends T>, String> map = new HashMap<Class<? extends T>, String>();
  private Map<String, Class<? extends T>> reverseMap = new HashMap<String, Class<? extends T>>();

  public void register(Class<? extends T> pCls)
  {
    TypeStringConverterRegistration a = pCls.getAnnotation(TypeStringConverterRegistration.class);
    if (a != null)
      register(pCls, a.value());
    else
      register(pCls, pCls.getSimpleName());
  }

  public void register(Class<? extends T> pCls, String pRepresentation)
  {
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
