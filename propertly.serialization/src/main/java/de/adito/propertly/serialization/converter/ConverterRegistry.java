package de.adito.propertly.serialization.converter;

import de.adito.picoservice.IPicoRegistry;
import de.adito.propertly.core.common.IFunction;
import de.adito.propertly.serialization.converter.impl.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.*;

/**
 * @author j.boesl, 05.03.15
 */
public class ConverterRegistry
{

  private final _Registry<IObjectConverter> registry;


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
    registry = new _Registry<IObjectConverter>();
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

  public IObjectConverter findObjectStringConverter(Class pCls)
  {
    IObjectConverter converter = registry.find(pCls);
    if (converter == null)
      throw new RuntimeException("No converter found for: " + pCls);
    return converter;
  }

  public IObjectConverter findTypeStringConverter(Class pCls)
  {
    IObjectConverter converter = registry.find(pCls);
    if (converter == null)
      throw new RuntimeException("No converter found for: " + pCls);
    return converter;
  }

  public String typeToString(Class pCls)
  {
    //noinspection unchecked
    return findTypeStringConverter(pCls).typeToString(pCls);
  }

  public Class stringToType(final String pTypeAsString)
  {
    IObjectConverter converter = registry.find(new IFunction<IObjectConverter, Boolean>()
    {
      @Override
      public Boolean run(IObjectConverter pTypeStringConverter)
      {
        return pTypeStringConverter.stringToType(pTypeAsString) != null;
      }
    });
    if (converter == null)
      throw new RuntimeException("No converter found for: " + pTypeAsString);
    return converter.stringToType(pTypeAsString);
  }

  /**
   * Registry
   */
  private static class _Registry<T extends IObjectConverter>
  {

    private final List<T> typeProviders;

    _Registry()
    {
      typeProviders = new ArrayList<T>();
    }

    synchronized void register(T pProvider)
    {
      Class commonType = pProvider.getCommonType();

      for (int i = 0; i < typeProviders.size(); i++)
      {
        T tp = typeProviders.get(i);
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
    T find(Class<?> pCls)
    {
      for (T typeProvider : typeProviders)
        //noinspection unchecked
        if (typeProvider.getCommonType().isAssignableFrom(pCls))
          return typeProvider;
      return null;
    }

    @Nullable
    T find(IFunction<T, Boolean> pPredicate)
    {
      for (T typeProvider : typeProviders)
        if (pPredicate.run(typeProvider))
          return typeProvider;
      return null;
    }
  }

}
