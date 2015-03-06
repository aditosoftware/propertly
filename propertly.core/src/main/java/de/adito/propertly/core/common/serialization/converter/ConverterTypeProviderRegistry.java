package de.adito.propertly.core.common.serialization.converter;

import de.adito.propertly.core.common.IFunction;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author j.boesl, 05.03.15
 */
public class ConverterTypeProviderRegistry<T extends IConverterTypeProvider>
{

  private final List<T> typeProviders;

  public ConverterTypeProviderRegistry()
  {
    typeProviders = new ArrayList<T>();
  }

  public synchronized void register(T pProvider)
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
  public T find(Class<?> pCls)
  {
    for (T typeProvider : typeProviders)
      //noinspection unchecked
      if (typeProvider.getCommonType().isAssignableFrom(pCls))
        return typeProvider;
    return null;
  }

  @Nullable
  public T find(IFunction<T, Boolean> pPredicate)
  {
    for (T typeProvider : typeProviders)
      if (pPredicate.run(typeProvider))
        return typeProvider;
    return null;
  }


}
