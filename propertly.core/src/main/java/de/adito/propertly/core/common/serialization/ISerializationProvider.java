package de.adito.propertly.core.common.serialization;

import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.*;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Is implemented to specify serialization and deserialization.
 *
 * @author j.boesl, 27.02.15
 */
public interface ISerializationProvider<F>
{

  <V> void serializeValue(
      @Nonnull F pParentOutputData, @Nonnull Class<V> pType, @Nonnull String pName,
      @Nullable List<? extends Annotation> pAnnotations, @Nullable V pValue);

  @Nonnull
  F serializeNode(
      @Nullable F pParentOutputData, @Nonnull Class<? extends IPropertyPitProvider> pType, @Nonnull String pName,
      @Nullable List<? extends Annotation> pAnnotations, @Nullable IPropertyPitProvider pValue,
      @Nonnull ChildRunner<F> pChildRunner);

  void deserialize(@Nonnull F pInputData, @Nonnull ChildAppender<F> pAppendChild);


  /**
   * @param <F>
   */
  interface ChildRunner<F>
  {
    void run(@Nonnull F pOutputData);
  }

  /**
   * @param <F>
   */
  interface ChildAppender<F>
  {
    <V> void appendValue(@Nonnull Class<V> pType, @Nonnull String pName, @Nullable List<? extends Annotation> pAnnotations,
                         @Nullable V pValue);

    void appendNode(@Nullable F pInputData, @Nonnull Class<? extends IPropertyPitProvider> pType, @Nonnull String pName,
                    @Nullable List<? extends Annotation> pAnnotations);
  }

}
