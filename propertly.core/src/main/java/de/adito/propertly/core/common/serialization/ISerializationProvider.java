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

  @Nonnull
  F serializeFixedNode(
      @Nonnull F pParentOutputData, @Nonnull String pName, @Nonnull IChildRunner<F> pChildRunner);

  @Nonnull
  F serializeFixedNode(
      @Nonnull F pParentOutputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pType,
      @Nonnull IChildRunner<F> pChildRunner);

  @Nonnull
  F serializeDynamicNode(
      @Nullable F pParentOutputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
      @Nullable List<? extends Annotation> pAnnotations, @Nonnull IChildRunner<F> pChildRunner);

  @Nonnull
  F serializeDynamicNode(
      @Nonnull F pParentOutputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
      @Nonnull Class<? extends IPropertyPitProvider> pType, @Nullable List<? extends Annotation> pAnnotations,
      @Nonnull IChildRunner<F> pChildRunner);

  void serializeFixedValue(
      @Nonnull F pParentOutputData, @Nonnull String pName, @Nullable Object pValue);

  <V> void serializeDynamicValue(
      @Nonnull F pParentOutputData, @Nonnull String pName, @Nonnull Class<? super V> pPropertyType, @Nullable V pValue,
      @Nullable List<? extends Annotation> pAnnotations);


  void deserialize(@Nonnull F pInputData, @Nonnull IChildAppender<F> pChildAppender);


  /**
   * @param <F>
   */
  interface IChildRunner<F>
  {
    void run(@Nonnull F pOutputData);
  }

  /**
   * @param <F>
   */
  interface IChildAppender<F>
  {
    @Nonnull
    IChildDetail getChildDetail(@Nonnull String pName);

    void appendFixedNode(
        @Nonnull F pInputData, @Nonnull String pName);

    void appendFixedNode(
        @Nonnull F pInputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pType);

    void appendDynamicNode(
        @Nonnull F pInputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
        @Nullable List<? extends Annotation> pAnnotations);

    void appendDynamicNode(
        @Nonnull F pInputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
        @Nonnull Class<? extends IPropertyPitProvider> pType, @Nullable List<? extends Annotation> pAnnotations);

    void appendFixedValue(
        @Nonnull String pName, @Nullable Object pValue);

    <V> void appendDynamicValue(
        @Nonnull String pName, @Nonnull Class<V> pPropertyType, @Nullable V pValue,
        @Nullable List<? extends Annotation> pAnnotations);
  }

  interface IChildDetail
  {
    @Nonnull
    EChildCategory getCategory();

    @Nonnull
    Class getType();
  }

  enum EChildCategory
  {
    FIXED_VALUE,
    FIXED_NODE,
    DYNAMIC
  }

}
