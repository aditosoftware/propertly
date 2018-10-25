package de.adito.propertly.serialization;

import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Is implemented to specify serialization and deserialization.
 *
 * @author j.boesl, 27.02.15
 */
public interface ISerializationProvider<T, F>
{

  @NotNull
  T serializeRootNode(
      @NotNull String pName, @NotNull Class<? extends IPropertyPitProvider> pPropertyType,
      @NotNull IChildRunner<F> pChildRunner);

  void serializeFixedNode(
      @NotNull F pParentOutputData, @NotNull String pName, @NotNull IChildRunner<F> pChildRunner);

  void serializeFixedNode(
      @NotNull F pParentOutputData, @NotNull String pName, @NotNull Class<? extends IPropertyPitProvider> pType,
      @NotNull IChildRunner<F> pChildRunner);

  void serializeDynamicNode(
      @NotNull F pParentOutputData, @NotNull String pName, @NotNull Class<? extends IPropertyPitProvider> pPropertyType,
      @Nullable List<? extends Annotation> pAnnotations, @NotNull IChildRunner<F> pChildRunner);

  void serializeDynamicNode(
      @NotNull F pParentOutputData, @NotNull String pName, @NotNull Class<? extends IPropertyPitProvider> pPropertyType,
      @NotNull Class<? extends IPropertyPitProvider> pType, @Nullable List<? extends Annotation> pAnnotations,
      @NotNull IChildRunner<F> pChildRunner);

  void serializeFixedValue(
      @NotNull F pParentOutputData, @NotNull String pName, @NotNull Object pValue);

  <V> void serializeDynamicValue(
      @NotNull F pParentOutputData, @NotNull String pName, @NotNull Class<? super V> pPropertyType, @Nullable V pValue,
      @Nullable List<? extends Annotation> pAnnotations);


  void deserializeRoot(@NotNull T pRootData, @NotNull IChildAppender<F> pChildAppender);

  void deserializeChild(@NotNull F pInputData, @NotNull IChildAppender<F> pChildAppender);


  /**
   * @param <F>
   */
  interface IChildRunner<F>
  {
    void run(@NotNull F pOutputData);
  }

  /**
   * @param <F>
   */
  interface IChildAppender<F>
  {
    @NotNull
    IChildDetail getChildDetail(@NotNull String pName);

    void appendFixedNode(
        @NotNull F pInputData, @NotNull String pName);

    void appendFixedNode(
        @NotNull F pInputData, @NotNull String pName, @NotNull Class<? extends IPropertyPitProvider> pType);

    void appendDynamicNode(
        @NotNull F pInputData, @NotNull String pName, @NotNull Class<? extends IPropertyPitProvider> pPropertyType,
        @Nullable List<? extends Annotation> pAnnotations);

    void appendDynamicNode(
        @NotNull F pInputData, @NotNull String pName, @NotNull Class<? extends IPropertyPitProvider> pPropertyType,
        @NotNull Class<? extends IPropertyPitProvider> pType, @Nullable List<? extends Annotation> pAnnotations);

    void appendFixedValue(
        @NotNull String pName, @Nullable Object pValue);

    <V> void appendDynamicValue(
        @NotNull String pName, @NotNull Class<V> pPropertyType, @Nullable V pValue,
        @Nullable List<? extends Annotation> pAnnotations);
  }

  interface IChildDetail
  {
    @NotNull
    EChildCategory getCategory();

    @NotNull
    Class getType();
  }

  enum EChildCategory
  {
    FIXED_VALUE,
    FIXED_NODE,
    DYNAMIC
  }

}
