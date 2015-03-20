package de.adito.propertly.serialization.converter;

import de.adito.propertly.core.common.IFunction;
import de.adito.propertly.serialization.converter.impl.*;

/**
 * @author j.boesl, 05.03.15
 */
public class ConverterRegistry
{

  private final ConverterTypeProviderRegistry<IObjectStringConverter> objectStringConverterRegistry;
  private final ConverterTypeProviderRegistry<ITypeStringConverter> typeStringConverterRegistry;


  public ConverterRegistry()
  {
    this(new BooleanStringConverter(),
         new ByteStringConverter(),
         new CharStringConverter(),
         new ColorStringConverter(),
         new DateStringConverter(),
         new DimensionStringConverter(),
         new DoubleStringConverter(),
         new EnumStringConverter(),
         new FloatStringConverter(),
         new FontStringConverter(),
         new IntegerStringConverter(),
         new LongStringConverter(),
         new ShortStringConverter(),
         new StringStringConverter(),
         new TypePPPStringConverter());
  }

  public ConverterRegistry(IConverterTypeProvider... pProviders)
  {
    objectStringConverterRegistry = new ConverterTypeProviderRegistry<IObjectStringConverter>();
    typeStringConverterRegistry = new ConverterTypeProviderRegistry<ITypeStringConverter>();
    if (pProviders != null)
      for (IConverterTypeProvider provider : pProviders)
        register(provider);
  }

  public void register(IConverterTypeProvider pProvider)
  {
    if (pProvider instanceof IObjectStringConverter)
      objectStringConverterRegistry.register((IObjectStringConverter) pProvider);
    if (pProvider instanceof ITypeStringConverter)
      typeStringConverterRegistry.register((ITypeStringConverter) pProvider);
  }

  public IObjectStringConverter findObjectStringConverter(Class pCls)
  {
    IObjectStringConverter converter = objectStringConverterRegistry.find(pCls);
    if (converter == null)
      throw new RuntimeException("No converter found for: " + pCls);
    return converter;
  }

  public ITypeStringConverter findTypeStringConverter(Class pCls)
  {
    ITypeStringConverter converter = typeStringConverterRegistry.find(pCls);
    if (converter == null)
      throw new RuntimeException("No converter found for: " + pCls);
    return converter;
  }

  public String typeToString(Class pCls)
  {
    return findTypeStringConverter(pCls).typeToString(pCls);
  }

  public Class stringToType(final String pTypeAsString)
  {
    ITypeStringConverter converter = typeStringConverterRegistry.find(new IFunction<ITypeStringConverter, Boolean>()
    {
      @Override
      public Boolean run(ITypeStringConverter pTypeStringConverter)
      {
        return pTypeStringConverter.stringToType(pTypeAsString) != null;
      }
    });
    if (converter == null)
      throw new RuntimeException("No converter found for: " + pTypeAsString);
    return converter.stringToType(pTypeAsString);
  }

}
