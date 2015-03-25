package de.adito.propertly.serialization.converter.impl;

import de.adito.propertly.serialization.converter.*;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public abstract class AbstractObjectStringConverter<T> implements IObjectConverter<T>
{
  private Class<T> cls;
  private String name;

  public AbstractObjectStringConverter(@Nonnull Class<T> pCls)
  {
    cls = pCls;
    name = pCls.getSimpleName();
  }

  @Nonnull
  @Override
  public Class<T> getCommonType()
  {
    return cls;
  }

  @Nullable
  @Override
  public Class<? extends T> stringToType(@Nonnull String pTypeAsString)
  {
    return name.equals(pTypeAsString) ? cls : null;
  }

  @Nonnull
  @Override
  public String typeToString(@Nonnull Class<? extends T> pType)
  {
    return name;
  }

  @Nullable
  @Override
  public T stringToValue(@Nonnull String pValueAsString, @Nonnull Class<? extends T> pType)
  {
    if (cls.equals(pType))
      return stringToValue(pValueAsString);
    throw new IllegalArgumentException(cls + " != " + pType);
  }

  @Nullable
  protected abstract T stringToValue(@Nonnull String pValueAsString);
}
