package de.adito.propertly.core.common;


import de.adito.propertly.core.spi.*;

import javax.annotation.Nonnull;
import java.util.Set;

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

  public static boolean isEqual(IPropertyPitProvider<?, ?, ?> p1, IPropertyPitProvider<?, ?, ?> p2)
  {
    IPropertyPit<? extends IPropertyPitProvider, ? extends IPropertyPitProvider<?, ?, ?>, ?> pit1 = p1.getPit();
    IPropertyPit<? extends IPropertyPitProvider, ? extends IPropertyPitProvider<?, ?, ?>, ?> pit2 = p2.getPit();
    Set<? extends IPropertyDescription> p1ds = pit1.getPropertyDescriptions();
    Set<? extends IPropertyDescription> p2ds = pit2.getPropertyDescriptions();
    if (!p1ds.equals(p2ds))
      return false;
    for (IProperty<?, ?> prop1 : pit1.getProperties())
    {
      IProperty<? extends IPropertyPitProvider<?, ?, ?>, ?> prop2 = pit2.findProperty(prop1.getDescription());
      if (!isEqual(prop1, prop2))
        return false;
    }
    return true;
  }

  public static boolean isEqual(IProperty p1, IProperty p2)
  {
    if (p1.equals(p2))
      return true;
    if (!p1.getDescription().equals(p2.getDescription()))
      return false;
    Object o1 = p1.getValue();
    Object o2 = p2.getValue();
    return o1 == o2 || o1 != null && o1.equals(o2)
        || o1 instanceof IPropertyPitProvider && o2 instanceof IPropertyPitProvider
        && isEqual((IPropertyPitProvider) o1, (IPropertyPitProvider) o2);
  }

}
