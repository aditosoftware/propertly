package de.adito.propertly.serialization.converter;

import de.adito.picoservice.IPicoRegistry;
import de.adito.propertly.serialization.converter.impl.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.*;

/**
 * @author j.boesl, 05.03.15
 */
public class ConverterRegistry
{

  private final _Registry registry;


  public ConverterRegistry()
  {
    this(new BooleanStringConverter(),
         new ByteStringConverter(),
         new CharStringConverter(),
         new ColorStringConverter(),
         new DateStringConverter(),
         new DimensionStringConverter(),
         new DoubleStringConverter(),
         new FloatStringConverter(),
         new FontStringConverter(),
         new IntegerStringConverter(),
         new LongStringConverter(),
         new ShortStringConverter(),
         new StringStringConverter(),
         new EnumStringConverter(),
         new TypePPPStringConverter());
  }

  public ConverterRegistry(IObjectConverter... pProviders)
  {
    registry = new _Registry();
    if (pProviders != null)
      for (IObjectConverter provider : pProviders)
        register(provider);
    registerProvided();
  }

  protected void registerProvided()
  {
    Map<Class<? extends IObjectConverter>, ConverterProvider> converterMap =
        IPicoRegistry.INSTANCE.find(IObjectConverter.class, ConverterProvider.class);
    for (Class<? extends IObjectConverter> converterClass : converterMap.keySet())
    {
      try
      {
        register(converterClass.newInstance());
      }
      catch (Exception e)
      {
        Logger.getLogger(getClass().getCanonicalName()).log(Level.WARNING, "Annotated wrong element with " +
            ConverterProvider.class.getSimpleName() + " at " + converterClass, e);
      }
    }
  }

  public void register(IObjectConverter pProvider)
  {
    registry.register(pProvider);
  }

  public <T> String valueToString(T pValue)
  {
    if (pValue == null)
      return "";
    //noinspection unchecked
    return _findObjectStringConverter((Class<T>) pValue.getClass()).valueToString(pValue);
  }

  public <T> T stringToValue(Class<T> pType, String pValueAsString)
  {
    return _findObjectStringConverter(pType).stringToValue(pValueAsString, pType);
  }

  public String typeToString(Class pType)
  {
    //noinspection unchecked
    return _findTypeStringConverter(pType).typeToString(pType);
  }

  public Class stringToType(Class pType, String pTypeAsString)
  {
    return _findTypeStringConverter(pType).stringToType(pTypeAsString);
  }

  public Class stringToType(String pTypeAsString)
  {
    IObjectConverter converter = registry.findByType(pTypeAsString);
    if (converter == null)
      throw new RuntimeException("No converter found for: " + pTypeAsString);
    return converter.stringToType(pTypeAsString);
  }

  private <T> IObjectConverter<T> _findObjectStringConverter(Class<T> pCls)
  {
    IObjectConverter<T> converter = registry.find(pCls);
    if (converter == null)
      throw new RuntimeException("No converter found for: " + pCls);
    return converter;
  }

  private IObjectConverter _findTypeStringConverter(Class pCls)
  {
    IObjectConverter converter = registry.find(pCls);
    if (converter == null)
      throw new RuntimeException("No converter found for: " + pCls);
    return converter;
  }

  /**
   * Registry
   */
  private static class _Registry
  {

    private final List<IObjectConverter> typeProviders;

    _Registry()
    {
      typeProviders = new ArrayList<IObjectConverter>();
    }

    synchronized void register(IObjectConverter<?> pProvider)
    {
      Class commonType = pProvider.getCommonType();

      for (int i = 0; i < typeProviders.size(); i++)
      {
        IObjectConverter tp = typeProviders.get(i);
        //noinspection unchecked
        if (tp.getCommonType().isAssignableFrom(commonType))
        {
          if (tp.getCommonType().equals(commonType))
            typeProviders.remove(i);
          typeProviders.add(i, pProvider);
          return;
        }
      }
      typeProviders.add(pProvider);
    }

    @Nullable
    <T> IObjectConverter<T> find(Class<T> pCls)
    {
      for (IObjectConverter<?> typeProvider : typeProviders)
        if (typeProvider.getCommonType().isAssignableFrom(pCls))
          //noinspection unchecked
          return (IObjectConverter<T>) typeProvider;
      return null;
    }

    @Nullable
    IObjectConverter<?> findByType(String pTypeName)
    {
      for (IObjectConverter<?> typeProvider : typeProviders)
        if (typeProvider.stringToType(pTypeName) != null)
          return typeProvider;
      return null;
    }
  }

}
