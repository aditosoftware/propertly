package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author j.boesl, 13.10.16
 */
public abstract class AbstractPropertyDescription<S extends IPropertyPitProvider, T> implements IPropertyDescription<S, T>
{
  private Class<S> sourceType;
  private Class<? extends T> type;
  private String name;
  private Map<Class<? extends Annotation>, Annotation> annotations;

  protected AbstractPropertyDescription(@NotNull Class<S> pSourceType, @NotNull Class<? extends T> pType,
                                        @NotNull String pName, @Nullable Iterable<? extends Annotation> pAnnotations)
  {
    sourceType = pSourceType;
    type = pType;
    name = pName;
    if (pAnnotations == null)
      annotations = Collections.emptyMap();
    else {
      annotations = new HashMap<>();
      for (Annotation annotation : pAnnotations)
        annotations.put(annotation.annotationType(), annotation);
    }
  }

  public AbstractPropertyDescription(@NotNull Class<S> pSourceType, @NotNull Class<? extends T> pType,
                                     @NotNull String pName, @Nullable Annotation... pAnnotations)
  {
    this(pSourceType, pType, pName, pAnnotations == null ? null : Arrays.asList(pAnnotations));
  }

  public AbstractPropertyDescription(@NotNull Class<S> pSourceType, @NotNull Class<? extends T> pType,
                                     @NotNull String pName)
  {
    this(pSourceType, pType, pName, (Iterable<? extends Annotation>) null);
  }

  public AbstractPropertyDescription(@NotNull IPropertyDescription<S, T> pPropertyDescription)
  {
    this(pPropertyDescription.getSourceType(), pPropertyDescription.getType(),
         pPropertyDescription.getName(), Arrays.asList(pPropertyDescription.getAnnotations()));
  }

  @Override
  public Class<S> getSourceType()
  {
    return sourceType;
  }

  @Override
  public Class<? extends T> getType()
  {
    return type;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> pAnnotationClass)
  {
    return annotations.containsKey(pAnnotationClass);
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> pAnnotationClass)
  {
    //noinspection unchecked
    return (A) annotations.get(pAnnotationClass);
  }

  @Override
  public Annotation[] getAnnotations()
  {
    Annotation[] arr = new Annotation[annotations.size()];
    int i = 0;
    for (Annotation annotation : annotations.values())
      arr[i++] = annotation;
    return arr;
  }

  @Override
  public Annotation[] getDeclaredAnnotations()
  {
    return getAnnotations();
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO)
      return true;
    if (!(pO instanceof AbstractPropertyDescription))
      return false;
    AbstractPropertyDescription<?, ?> that = (AbstractPropertyDescription<?, ?>) pO;
    return Objects.equals(getSourceType(), that.getSourceType()) &&
        Objects.equals(getType(), that.getType()) &&
        Objects.equals(getName(), that.getName()) &&
        Arrays.equals(getAnnotations(), that.getAnnotations());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(getSourceType(), getType(), getName(), getAnnotations());
  }

}
