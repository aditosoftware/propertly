package de.adito.propertly.serialization.converter.impl;

import de.adito.propertly.serialization.converter.IObjectConverter;

import javax.annotation.*;
import java.util.*;

/**
 * @author j.boesl, 04.03.15
 */
public abstract class AbstractObjectConverter<S> implements IObjectConverter<S>
{
  private Class<S> cls;
  private String name;
  private List<SourceTargetConverter<S, ?>> sourceTargetConverters;

  public AbstractObjectConverter(@Nonnull Class<S> pCls)
  {
    this(pCls, pCls.getSimpleName());
  }

  public AbstractObjectConverter(@Nonnull Class<S> pCls, @Nonnull String pName)
  {
    cls = pCls;
    name = pName;
    sourceTargetConverters = new ArrayList<SourceTargetConverter<S, ?>>();

    registerSourceTargetConverter(new SourceTargetConverter<S, S>(cls)
    {
      @Nonnull
      @Override
      public S sourceToTarget(@Nonnull S pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public S targetToSource(@Nonnull S pTarget)
      {
        return pTarget;
      }
    });
  }

  @Nonnull
  @Override
  public Class<S> getCommonType()
  {
    return cls;
  }

  @Nullable
  @Override
  public Class<? extends S> stringToType(@Nonnull String pTypeAsString)
  {
    return name.equals(pTypeAsString) ? cls : null;
  }

  @Nonnull
  @Override
  public String typeToString(@Nonnull Class<? extends S> pType)
  {
    return name;
  }

  @Nullable
  @Override
  public S targetToSource(@Nonnull Object pTarget, @Nonnull Class<? extends S> pSourceType)
  {
    if (!cls.equals(pSourceType))
      throw new IllegalArgumentException(cls + " != " + pSourceType);

    for (SourceTargetConverter<S, ?> sourceTargetConverter : sourceTargetConverters)
      if (sourceTargetConverter.getSupportedTargetType().isAssignableFrom(pTarget.getClass()))
        //noinspection unchecked
        return ((SourceTargetConverter<S, Object>) sourceTargetConverter).targetToSource(pTarget);
    throw new IllegalArgumentException(pTarget.getClass() + " is not convertible to " + pSourceType + ".");
  }

  @Nonnull
  @Override
  public Object sourceToTarget(@Nonnull S pSource, @Nonnull Class... pTargetTypes)
  {
    for (Class targetType : pTargetTypes)
      for (SourceTargetConverter<S, ?> sourceTargetConverter : sourceTargetConverters)
        if (sourceTargetConverter.getSupportedTargetType().isAssignableFrom(targetType))
          return sourceTargetConverter.sourceToTarget(pSource);
    throw new IllegalArgumentException("no supported target type for " + cls + ".");
  }

  protected void registerSourceTargetConverter(@Nonnull SourceTargetConverter<S, ?> pSourceTargetConverter)
  {
    Class<?> targetType = pSourceTargetConverter.getSupportedTargetType();
    for (int i = 0; i < sourceTargetConverters.size(); i++)
    {
      SourceTargetConverter<S, ?> sourceTargetConverter = sourceTargetConverters.get(i);
      if (sourceTargetConverter.getSupportedTargetType().isAssignableFrom(targetType))
      {
        if (sourceTargetConverter.getSupportedTargetType().equals(targetType))
          sourceTargetConverters.remove(i);
        sourceTargetConverters.add(i, pSourceTargetConverter);
        return;
      }
    }
    sourceTargetConverters.add(pSourceTargetConverter);
  }

  /**
   * For registration of specific converters.
   *
   * @param <S> Source
   * @param <T> Target
   */
  protected static abstract class SourceTargetConverter<S, T>
  {
    private Class<T> supportedTargetType;

    protected SourceTargetConverter(@Nonnull Class<T> pSupportedTargetType)
    {
      supportedTargetType = pSupportedTargetType;
    }

    @Nonnull
    public Class<T> getSupportedTargetType()
    {
      return supportedTargetType;
    }

    @Nonnull
    public abstract T sourceToTarget(@Nonnull S pSource);

    @Nullable
    public abstract S targetToSource(@Nonnull T pTarget);
  }

}
