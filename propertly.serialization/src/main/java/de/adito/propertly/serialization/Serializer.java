package de.adito.propertly.serialization;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.api.PropertyDescription;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Can serialize and deserialize IPropertyPitProviders. Output format is dependent on used ISerializationProvider
 * implementation.
 *
 * @author j.boesl, 27.02.15
 */
public class Serializer<T>
{

  public static <T> Serializer<T> create(ISerializationProvider<T, ?> pSerializationProvider)
  {
    return new Serializer<T>(pSerializationProvider);
  }


  private ISerializationProvider<T, ?> sp;

  Serializer(ISerializationProvider<T, ?> pSerializationProvider)
  {
    sp = pSerializationProvider;
  }


  @Nonnull
  public T serialize(@Nonnull IHierarchy<?> pHierarchy)
  {
    IPropertyPitProvider<?, ?, ?> value = pHierarchy.getValue();
    return serialize(value);
  }

  @Nonnull
  public T serialize(@Nonnull IPropertyPitProvider<?, ?, ?> pPropertyPitProvider)
  {
    ISerializationProvider.IChildRunner<Object> childRunner = new _ChildRunner(pPropertyPitProvider);
    return ((ISerializationProvider<T, Object>) sp).serializeRootNode(
        pPropertyPitProvider.getPit().getOwnProperty().getName(), pPropertyPitProvider.getClass(), childRunner);
  }

  @Nonnull
  public IPropertyPitProvider deserialize(@Nonnull T pData)
  {
    _ChildAppender<?> childAppender = new _ChildAppender(null);
    sp.deserializeRoot(pData, (_ChildAppender) childAppender);
    return childAppender.property.getValue();
  }

  /**
   * ChildRunner implementation.
   */
  private class _ChildRunner<F> implements ISerializationProvider.IChildRunner<F>
  {
    private IPropertyPitProvider<?, ?, ?> ppp;

    _ChildRunner(IPropertyPitProvider<?, ?, ?> pPropertyPitProvider)
    {
      ppp = pPropertyPitProvider;
    }

    @Override
    public void run(@Nonnull F pOutputData)
    {
      for (IProperty property : ppp.getPit().getProperties())
      {
        IPropertyDescription descr = property.getDescription();
        String name = descr.getName();
        Class type = descr.getType();
        Object value = property.getValue();
        if (IPropertyPitProvider.class.isAssignableFrom(type))
          _serialize(pOutputData, (IPropertyPitProvider<?, ?, ?>) value);
        else if (property.isDynamic())
        {
          Annotation[] arr = descr.getAnnotations();
          List<? extends Annotation> annotations =
              arr == null || arr.length == 0 ? Collections.<Annotation>emptyList() : Arrays.asList(arr);
          _getSerializationProvider().serializeDynamicValue(pOutputData, name, type, value, annotations);
        }
        else if (value != null)
          _getSerializationProvider().serializeFixedValue(pOutputData, name, value);
      }
    }

    private void _serialize(F pOutputData, IPropertyPitProvider<?, ?, ?> pPPP)
    {
      if (pPPP == null)
        return;

      IProperty<? extends IPropertyPitProvider, ? extends IPropertyPitProvider> property = pPPP.getPit().getOwnProperty();
      IPropertyDescription<?, ? extends IPropertyPitProvider> descr = property.getDescription();
      String name = descr.getName();
      Class<? extends IPropertyPitProvider> type = descr.getType();

      ISerializationProvider.IChildRunner childRunner = new _ChildRunner(pPPP);
      if (property.isDynamic())
      {
        Annotation[] arr = descr.getAnnotations();
        List<? extends Annotation> annotations =
            arr == null || arr.length == 0 ? Collections.<Annotation>emptyList() : Arrays.asList(arr);
        if (pPPP.getClass().equals(type))
          _getSerializationProvider().serializeDynamicNode(pOutputData, name, type, annotations, childRunner);
        else
          _getSerializationProvider().serializeDynamicNode(pOutputData, name, type, pPPP.getClass(), annotations, childRunner);
      }
      else
      {
        if (pPPP.getClass().equals(type))
          _getSerializationProvider().serializeFixedNode(pOutputData, name, childRunner);
        else
          _getSerializationProvider().serializeFixedNode(pOutputData, name, pPPP.getClass(), childRunner);
      }
    }

    private ISerializationProvider<T, F> _getSerializationProvider()
    {
      return (ISerializationProvider<T, F>) sp;
    }
  }

  /**
   * ChildAppender implementation.
   */
  private class _ChildAppender<F> implements ISerializationProvider.IChildAppender<F>
  {
    IProperty<?, IPropertyPitProvider> property;

    _ChildAppender(IProperty<?, IPropertyPitProvider> pProperty)
    {
      property = pProperty;
    }

    @Nonnull
    @Override
    public ISerializationProvider.IChildDetail getChildDetail(@Nonnull String pName)
    {
      Class<?> type = Object.class;
      if (property != null)
      {
        IPropertyPitProvider<?, ?, ?> ppp = property.getValue();
        if (ppp != null)
        {
          IProperty<? extends IPropertyPitProvider<?, ?, ?>, ?> childProperty =
              ppp.getPit().findProperty(PropertyDescription.create(IPropertyPitProvider.class, Object.class, pName));

          if (childProperty != null)
          {
            type = childProperty.getType();
            if (!childProperty.isDynamic())
              return new _ChildDetail(IPropertyPitProvider.class.isAssignableFrom(type) ?
                  ISerializationProvider.EChildCategory.FIXED_NODE :
                  ISerializationProvider.EChildCategory.FIXED_VALUE, type);
          }
          else
            type = ppp.getPit().getChildType();
        }
      }

      return new _ChildDetail(ISerializationProvider.EChildCategory.DYNAMIC, type);
    }

    @Override
    public void appendFixedNode(
        @Nonnull F pInputData, @Nonnull String pName)
    {
      _appendFixedNode(pInputData, pName, null);
    }

    @Override
    public void appendFixedNode(
        @Nonnull F pInputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pType)
    {
      _appendFixedNode(pInputData, pName, pType);
    }

    @Override
    public void appendDynamicNode(
        @Nonnull F pInputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
        @Nullable List<? extends Annotation> pAnnotations)
    {
      _appendDynamicNode(pInputData, pName, pPropertyType, null, pAnnotations);
    }

    @Override
    public void appendDynamicNode(
        @Nonnull F pInputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
        @Nonnull Class<? extends IPropertyPitProvider> pType, @Nullable List<? extends Annotation> pAnnotations)
    {
      _appendDynamicNode(pInputData, pName, pPropertyType, pType, pAnnotations);
    }

    @Override
    public void appendFixedValue(
        @Nonnull String pName, @Nullable Object pValue)
    {
      IProperty<?, Object> prop = _getProperty(pName, Object.class);
      prop.setValue(pValue);
    }

    @Override
    public <V> void appendDynamicValue(
        @Nonnull String pName, @Nonnull Class<V> pPropertyType, @Nullable V pValue,
        @Nullable List<? extends Annotation> pAnnotations)
    {
      IMutablePropertyPitProvider<?, ?, ? super V> mppp = _getMutablePropertyPitProvider();
      IProperty<?, V> prop = mppp.getPit().addProperty(pPropertyType, pName, pAnnotations);
      prop.setValue(pValue);
    }

    private void _appendFixedNode(
        @Nonnull F pInputData, @Nonnull String pName, @Nullable Class<? extends IPropertyPitProvider> pType)
    {
      IProperty<?, IPropertyPitProvider> prop = _getProperty(pName, IPropertyPitProvider.class);
      prop.setValue(PropertlyUtility.create(pType == null ? prop.getType() : pType));
      _deserialize(pInputData, prop);
    }

    public void _appendDynamicNode(
        @Nonnull F pInputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
        @Nullable Class<? extends IPropertyPitProvider> pType, @Nullable List<? extends Annotation> pAnnotations)
    {
      IPropertyPitProvider<?, ?, IPropertyPitProvider> ppp = PropertlyUtility.create(pType == null ? pPropertyType : pType);
      if (property == null)
      {
        Hierarchy<IPropertyPitProvider> hierarchy = new Hierarchy<IPropertyPitProvider>(pName, ppp);
        property = hierarchy.getProperty();
        _deserialize(pInputData, property);
      }
      else
      {
        IMutablePropertyPitProvider<?, ?, ? super IPropertyPitProvider> mppp = _getMutablePropertyPitProvider();
        IProperty<?, IPropertyPitProvider> prop =
            (IProperty<?, IPropertyPitProvider>) mppp.getPit().addProperty(pPropertyType, pName, pAnnotations);
        prop.setValue(ppp);
        _deserialize(pInputData, prop);
      }
    }

    private <F> IPropertyPitProvider _deserialize(F pData, IProperty<?, IPropertyPitProvider> pProperty)
    {
      _ChildAppender<F> childAppender = new _ChildAppender<F>(pProperty);
      ((ISerializationProvider<T, F>) sp).deserializeChild(pData, childAppender);
      return childAppender.property.getValue();
    }

    private IMutablePropertyPitProvider _getMutablePropertyPitProvider()
    {
      return (IMutablePropertyPitProvider) _getPropertyPitProvider();
    }

    @Nonnull
    private IPropertyPitProvider _getPropertyPitProvider()
    {
      Objects.requireNonNull(property);
      IPropertyPitProvider ppp = property.getValue();
      Objects.requireNonNull(ppp);
      return ppp;
    }

    @Nonnull
    private <T> IProperty<?, T> _getProperty(String pName, Class<T> pType)
    {
      IPropertyPitProvider ppp = _getPropertyPitProvider();
      IPropertyDescription<?, T> pd = PropertyDescription.create(IPropertyPitProvider.class, pType, pName);
      return ppp.getPit().getProperty(pd);
    }
  }

  /**
   * IChildDetail implementation
   */
  private static class _ChildDetail implements ISerializationProvider.IChildDetail
  {
    private ISerializationProvider.EChildCategory category;
    private Class type;

    public _ChildDetail(ISerializationProvider.EChildCategory pCategory, Class pType)
    {
      category = pCategory;
      type = pType;
    }

    @Nonnull
    @Override
    public ISerializationProvider.EChildCategory getCategory()
    {
      return category;
    }

    @Nonnull
    @Override
    public Class getType()
    {
      return type;
    }
  }

}