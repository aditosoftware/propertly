package de.adito.propertly.core.common.serialization;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Can serialize and deserialize IPropertyPitProviders. Output format is dependent on used ISerializationProvider
 * implementation.
 *
 * @author j.boesl, 27.02.15
 */
public class Serializer<F>
{

  public static <F> Serializer<F> create(ISerializationProvider<F> pSerializationProvider)
  {
    return new Serializer<F>(pSerializationProvider);
  }


  private ISerializationProvider<F> sp;

  Serializer(ISerializationProvider<F> pSerializationProvider)
  {
    sp = pSerializationProvider;
  }


  @Nonnull
  public F serialize(@Nonnull IHierarchy pHierarchy)
  {
    return serialize(pHierarchy.getValue());
  }

  @Nonnull
  public F serialize(@Nonnull IPropertyPitProvider<?, ?, ?> pPropertyPitProvider)
  {
    return _serialize(null, pPropertyPitProvider);
  }

  @Nonnull
  public IPropertyPitProvider deserialize(@Nonnull F pData)
  {
    return _deserialize(pData, null);
  }

  private F _serialize(F pOutputData, IPropertyPitProvider<?, ?, ?> pPPP)
  {
    ISerializationProvider.IChildRunner<F> childRunner = new _ChildRunner(pPPP);
    IProperty<? extends IPropertyPitProvider, ? extends IPropertyPitProvider> property = pPPP.getPit().getOwnProperty();
    IPropertyDescription<?, ? extends IPropertyPitProvider> descr = property.getDescription();
    String name = descr.getName();
    Class<? extends IPropertyPitProvider> type = descr.getType();
    if (property.isDynamic())
    {
      List<? extends Annotation> annotations = descr.getAnnotations();
      if (annotations.isEmpty())
        annotations = null;
      if (pPPP.getClass().equals(type))
        return sp.serializeDynamicNode(pOutputData, name, type, annotations, childRunner);
      sp.serializeDynamicNode(pOutputData, name, type, pPPP.getClass(), annotations, childRunner);
      return pOutputData;
    }
    else
    {
      if (pPPP.getClass().equals(type))
        sp.serializeFixedNode(pOutputData, name, childRunner);
      else
        sp.serializeFixedNode(pOutputData, name, pPPP.getClass(), childRunner);
      return pOutputData;
    }
  }

  private IPropertyPitProvider _deserialize(F pData, IProperty<?, IPropertyPitProvider> pProperty)
  {
    _ChildAppender childAppender = new _ChildAppender(pProperty);
    sp.deserialize(pData, childAppender);
    return childAppender.property.getValue();
  }

  /**
   * ChildRunner implementation.
   */
  private class _ChildRunner implements ISerializationProvider.IChildRunner<F>
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
          List<? extends Annotation> annotations = descr.getAnnotations();
          if (annotations.isEmpty())
            annotations = null;
          sp.serializeDynamicValue(pOutputData, name, type, value, annotations);
        }
        else if (value != null)
          sp.serializeFixedValue(pOutputData, name, value);
      }
    }
  }

  /**
   * ChildAppender implementation.
   */
  private class _ChildAppender implements ISerializationProvider.IChildAppender<F>
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
