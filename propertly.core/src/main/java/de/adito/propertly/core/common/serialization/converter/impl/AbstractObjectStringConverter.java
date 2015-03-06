package de.adito.propertly.core.common.serialization.converter.impl;

import de.adito.propertly.core.common.serialization.converter.*;

import javax.annotation.*;

/**
 * @author j.boesl, 04.03.15
 */
public abstract class AbstractObjectStringConverter<T> implements ITypeStringConverter<T>, IObjectStringConverter<T>
{
  private Class<T> cls;

  public AbstractObjectStringConverter(@Nonnull Class<T> pCls)
  {
    cls = pCls;
  }

  @Nonnull
  @Override
  public Class<? super T> getCommonType()
  {
    return cls;
  }

  @Nullable
  @Override
  public Class<? extends T> stringToType(@Nonnull String pTypeAsString)
  {
    return cls.getSimpleName().equals(pTypeAsString) ? cls : null;
  }

  @Nonnull
  @Override
  public String typeToString(@Nonnull Class<? extends T> pType)
  {
    return pType.getSimpleName();
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
