package de.adito.propertly.preset;

import de.adito.propertly.core.api.PropertyDescription;
import de.adito.propertly.core.spi.*;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author j.boesl, 09.09.16
 */
public class PresetPropertyDescription<S extends IPropertyPitProvider, T>
    implements IPresetPropertyDescription<S, T>
{
  private IPropertyDescription<S, T> delegate;
  private IPresetSupplier<S, T> presetSupplier;

  protected PresetPropertyDescription(Class<S> pSourceType, Class<? extends T> pType, String pName,
                                      Iterable<? extends Annotation> pAnnotations, IPresetSupplier<S, T> pPresetSupplier)
  {
    this(PropertyDescription.create(pSourceType, pType, pName, pAnnotations), pPresetSupplier);
  }

  protected PresetPropertyDescription(IPropertyDescription<S, T> pDescription, IPresetSupplier<S, T> pPresetSupplier)
  {
    delegate = pDescription;
    presetSupplier = pPresetSupplier;
  }

  @Override
  public Class<S> getSourceType()
  {
    return delegate.getSourceType();
  }

  @Override
  public Class<? extends T> getType()
  {
    return delegate.getType();
  }

  @Override
  public String getName()
  {
    return delegate.getName();
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> pAnnotationClass)
  {
    return delegate.getAnnotation(pAnnotationClass);
  }

  @Override
  public Annotation[] getAnnotations()
  {
    return delegate.getAnnotations();
  }

  @Override
  public Annotation[] getDeclaredAnnotations()
  {
    return delegate.getDeclaredAnnotations();
  }

  @Override
  public T getPreset()
  {
    return presetSupplier.get(this);
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO)
      return true;
    if (!(pO instanceof PresetPropertyDescription))
      return false;
    PresetPropertyDescription<?, ?> that = (PresetPropertyDescription<?, ?>) pO;
    return Objects.equals(delegate, that.delegate) &&
        Objects.equals(presetSupplier, that.presetSupplier);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(delegate, presetSupplier);
  }

  @Override
  public String toString()
  {
    String presetValueString;
    try {
      presetValueString = Objects.toString(presetSupplier.get(this));
    }
    catch (Exception pE) {
      presetValueString = pE.getClass().getSimpleName() + ": " + pE.getMessage();
    }

    return "PresetPropertyDescription{" +
        "type='" + (delegate.getType() == null ? null : delegate.getType().getSimpleName()) +
        "', name='" + delegate.getName() + '\'' +
        "', preset='" + presetValueString + '\'' +
        '}';
  }

}
