package de.adito.propertly.core.common.serialization;

import de.adito.propertly.core.api.*;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.lang.annotation.Annotation;
import java.util.List;

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


  public F serialize(IHierarchy pHierarchy)
  {
    return serialize(pHierarchy.getValue());
  }

  public F serialize(IPropertyPitProvider<?, ?, ?> pPropertyPitProvider)
  {
    return _serialize(null, pPropertyPitProvider);
  }

  public IPropertyPitProvider deserialize(F pData)
  {
    return _deserialize(pData, null);
  }

  private F _serialize(F pOutputData, final IPropertyPitProvider<?, ?, ?> pPPP)
  {
    ISerializationProvider.ChildRunner<F> childRunner = new _ChildRunner(pPPP);
    IPropertyDescription<?, ? extends IPropertyPitProvider> descr = pPPP.getPit().getOwnProperty().getDescription();
    return sp.serializeNode(pOutputData, descr.getType(), descr.getName(), descr.getAnnotations(), pPPP, childRunner);
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
  private class _ChildRunner implements ISerializationProvider.ChildRunner<F>
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
        if (IPropertyPitProvider.class.isAssignableFrom(descr.getType()))
          _serialize(pOutputData, (IPropertyPitProvider<?, ?, ?>) property.getValue());
        else
          sp.serializeValue(pOutputData, descr.getType(), descr.getName(), descr.getAnnotations(), property.getValue());
      }
    }
  }

  /**
   * ChildAppender implementation.
   */
  private class _ChildAppender implements ISerializationProvider.ChildAppender<F>
  {
    IProperty<?, IPropertyPitProvider> property;

    _ChildAppender(IProperty<?, IPropertyPitProvider> pProperty)
    {
      property = pProperty;
    }

    @Override
    public void appendNode(@Nullable F pInputData, @Nonnull Class<? extends IPropertyPitProvider> pType,
                           @Nonnull String pName, @Nullable List<? extends Annotation> pAnnotations)
    {
      if (property == null)
      {
        assert IPropertyPitProvider.class.isAssignableFrom(pType);
        property = new Hierarchy<IPropertyPitProvider>(pName, PropertlyUtility.create(pType)).getProperty();
        _deserialize(pInputData, property);
      }
      else
      {
        IProperty<?, IPropertyPitProvider> property = _put(pType, pName, pAnnotations, null);
        _deserialize(pInputData, property);
      }
    }

    @Override
    public <V> void appendValue(@Nonnull Class<V> pType, @Nonnull String pName,
                                @Nullable List<? extends Annotation> pAnnotations, @Nullable V pValue)
    {
      _put(pType, pName, pAnnotations, pValue);
    }

    private <T> IProperty<?, T> _put(@Nonnull Class<? extends T> pType, @Nonnull String pName,
                                     @Nullable List<? extends Annotation> pAnnotations, @Nullable T pValue)
    {
      assert property != null;
      IPropertyPitProvider<?, ?, ?> ppp = property.getValue();
      if (ppp == null)
      {
        property.setValue(PropertlyUtility.create(property.getType()));
        ppp = property.getValue();
      }

      IPropertyPit<? extends IPropertyPitProvider, ? extends IPropertyPitProvider, ?> pit = ppp.getPit();
      IPropertyDescription<?, T> pd = PropertyDescription.create(IPropertyPitProvider.class, pType, pName, pAnnotations);
      IProperty<?, T> property = pit.findProperty(pd);
      if (property == null)
      {
        if (pit instanceof IMutablePropertyPit)
        {
          pd = PropertyDescription.create(pit.getOwnProperty().getType(), pType, pName, pAnnotations);
          //noinspection unchecked
          property = ((IMutablePropertyPit) pit).addProperty(pd);
        }
        else
          throw new RuntimeException("can't restore property: " + pd);
      }
      property.setValue(pValue);
      return property;
    }
  }

}
