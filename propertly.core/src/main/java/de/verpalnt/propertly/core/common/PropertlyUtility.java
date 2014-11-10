package de.verpalnt.propertly.core.common;


import javax.annotation.Nonnull;

/**
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
