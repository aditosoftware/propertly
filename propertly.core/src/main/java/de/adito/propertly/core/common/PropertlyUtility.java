package de.adito.propertly.core.common;


import de.adito.propertly.core.spi.IPropertyPitProvider;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * An Utility class with common functions.
 *
 * @author PaL
 *         Date: 07.04.13
 *         Time: 14:23
 */
public class PropertlyUtility
{

  private PropertlyUtility()
  {
  }

  public static <T> ISupplier<T> getFixedSupplier(final T pInstance)
  {
    return new ISupplier<T>()
    {
      @Override
      public T get()
      {
        return pInstance;
      }
    };
  }

  public static <T extends IPropertyPitProvider> T create(@Nonnull T pPropertyPitProvider)
  {
    if (pPropertyPitProvider instanceof IReconstructor)
      //noinspection unchecked
      return ((IReconstructor<T>) pPropertyPitProvider).create();

    //noinspection unchecked
    Class<T> cls = (Class<T>) pPropertyPitProvider.getClass();
    return create(cls);
  }

  public static <T extends IPropertyPitProvider> T create(@Nonnull Class<T> pClass)
  {
    try
    {
      return pClass.newInstance();
    }
    catch (Exception e)
    {
      throw new RuntimeException("can't instantiate: " + pClass, e);
    }
  }

  public static String asString(@Nonnull Object pObj, String... pDetails)
  {
    StringBuilder strBuilder = new StringBuilder()
        .append(pObj.getClass().getSimpleName())
        .append("@")
        .append(Integer.toHexString(pObj.hashCode()));
    if (pDetails != null && pDetails.length != 0)
    {
      StringBuilder detailsBuilder = new StringBuilder();
      for (String detail : pDetails)
      {
        if (detailsBuilder.length() != 0)
          detailsBuilder.append(", ");
        detailsBuilder.append(detail);
      }
      strBuilder.append('{').append(detailsBuilder).append('}');
    }
    return strBuilder.toString();
  }

}
