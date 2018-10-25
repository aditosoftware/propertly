package de.adito.propertly.serialization.converter.impl;

import de.adito.propertly.serialization.converter.IObjectConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author j.boesl, 04.03.15
 */
public abstract class AbstractObjectConverter<S> implements IObjectConverter<S>
{
  private Class<S> cls;
  private String name;
  private List<SourceTargetConverter<S, ?>> sourceTargetConverters;

  public AbstractObjectConverter(@NotNull Class<S> pCls)
  {
    this(pCls, pCls.getSimpleName());
  }

  public AbstractObjectConverter(@NotNull Class<S> pCls, @NotNull String pName)
  {
    cls = pCls;
    name = pName;
    sourceTargetConverters = new ArrayList<>();

    registerSourceTargetConverter(new SourceTargetConverter<S, S>(cls)
    {
      @NotNull
      @Override
      public S sourceToTarget(@NotNull S pSource)
      {
        return pSource;
      }

      @Nullable
      @Override
      public S targetToSource(@NotNull S pTarget)
      {
        return pTarget;
      }
    });
  }

  @NotNull
  @Override
  public Class<S> getCommonType()
  {
    return cls;
  }

  @Nullable
  @Override
  public Class<? extends S> stringToType(@NotNull String pTypeAsString)
  {
    return name.equals(pTypeAsString) ? cls : null;
  }

  @NotNull
  @Override
  public String typeToString(@NotNull Class<? extends S> pType)
  {
    return name;
  }

  @Nullable
  @Override
  public S targetToSource(@NotNull Object pTarget, @NotNull Class<? extends S> pSourceType)
  {
    if (!cls.equals(pSourceType))
      throw new IllegalArgumentException(cls + " != " + pSourceType);

    for (SourceTargetConverter<S, ?> sourceTargetConverter : sourceTargetConverters)
      if (sourceTargetConverter.getSupportedTargetType().isAssignableFrom(pTarget.getClass()))
        //noinspection unchecked
        return ((SourceTargetConverter<S, Object>) sourceTargetConverter).targetToSource(pTarget);
    throw new IllegalArgumentException(pTarget.getClass() + " is not convertible to " + pSourceType + ".");
  }

  @NotNull
  @Override
  public Object sourceToTarget(@NotNull S pSource, @NotNull Class... pTargetTypes)
  {
    for (Class targetType : pTargetTypes)
      for (SourceTargetConverter<S, ?> sourceTargetConverter : sourceTargetConverters)
        if (sourceTargetConverter.getSupportedTargetType().isAssignableFrom(targetType))
          return sourceTargetConverter.sourceToTarget(pSource);
    throw new IllegalArgumentException("no supported target type for " + cls + ".");
  }

  protected void registerSourceTargetConverter(@NotNull SourceTargetConverter<S, ?> pSourceTargetConverter)
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

    protected SourceTargetConverter(@NotNull Class<T> pSupportedTargetType)
    {
      supportedTargetType = pSupportedTargetType;
    }

    @NotNull
    public Class<T> getSupportedTargetType()
    {
      return supportedTargetType;
    }

    @NotNull
    public abstract T sourceToTarget(@NotNull S pSource);

    @Nullable
    public abstract S targetToSource(@NotNull T pTarget);
  }

}
