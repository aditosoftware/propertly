package de.adito.propertly.core.common;


import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPit;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * An Utility class with common functions.
 *
 * @author PaL
 * Date: 07.04.13
 * Time: 14:23
 */
public class PropertlyUtility
{

  private PropertlyUtility()
  {
  }

  public static <T> Supplier<T> getFixedSupplier(final T pInstance)
  {
    return () -> pInstance;
  }

  public static <T extends IPropertyPitProvider> T create(@NotNull T pPropertyPitProvider)
  {
    if (pPropertyPitProvider instanceof IReconstructor)
      //noinspection unchecked
      return ((IReconstructor<T>) pPropertyPitProvider).create();

    //noinspection unchecked
    Class<T> cls = (Class<T>) pPropertyPitProvider.getClass();
    return create(cls);
  }

  public static <T extends IPropertyPitProvider> T create(@NotNull Class<T> pClass)
  {
    try {
      return (T) MethodHandles.lookup().findConstructor(pClass, MethodType.methodType(void.class)).invoke();
    }
    catch (Throwable e) {
      throw new RuntimeException("can't instantiate: " + pClass, e);
    }
  }

  @SafeVarargs
  public static <T> Set<T> toNonnullSet(T... pTs)
  {
    if (pTs == null || pTs.length == 0)
      return Collections.emptySet();
    if (pTs.length == 1)
      return Collections.singleton(pTs[0]);
    return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(pTs)));
  }

  public static String asString(@NotNull Object pObj, String... pDetails)
  {
    StringBuilder strBuilder = new StringBuilder()
        .append(pObj.getClass().getSimpleName())
        .append("@")
        .append(Integer.toHexString(pObj.hashCode()));
    if (pDetails != null && pDetails.length != 0) {
      StringBuilder detailsBuilder = new StringBuilder();
      for (String detail : pDetails) {
        if (detailsBuilder.length() != 0)
          detailsBuilder.append(", ");
        detailsBuilder.append(detail);
      }
      strBuilder.append('{').append(detailsBuilder).append('}');
    }
    return strBuilder.toString();
  }

  public static boolean isEqual(@Nullable IPropertyPitProvider<?, ?, ?> p1, @Nullable IPropertyPitProvider<?, ?, ?> p2)
  {
    if (p1 == p2)
      return true;
    if (p1 == null || p2 == null)
      return false;

    IPropertyPit<? extends IPropertyPitProvider, ? extends IPropertyPitProvider<?, ?, ?>, ?> pit1 = p1.getPit();
    IPropertyPit<? extends IPropertyPitProvider, ? extends IPropertyPitProvider<?, ?, ?>, ?> pit2 = p2.getPit();
    Set<? extends IPropertyDescription> p1ds = pit1.getPropertyDescriptions();
    Set<? extends IPropertyDescription> p2ds = pit2.getPropertyDescriptions();
    if (!p1ds.equals(p2ds))
      return false;
    for (IProperty<?, ?> prop1 : pit1.getProperties()) {
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
