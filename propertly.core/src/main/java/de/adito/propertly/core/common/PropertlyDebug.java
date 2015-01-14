package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.*;

/**
 * Little helper class for debugging.
 *
 * @author j.boesl, 14.01.15
 */
public class PropertlyDebug
{

  /**
   * Transforms given IHierarchy to a readable string.
   *
   * @param pHierarchy the starting point.
   * @return the whole tree as a string.
   */
  public static String toTreeString(IHierarchy<?> pHierarchy)
  {
    return toTreeString(pHierarchy.getProperty());
  }

  /**
   * Transforms given IPropertyPitProvider to a readable string.
   *
   * @param pPitProvider the starting point.
   * @return the subtree starting at the supplied IPropertyPitProvider as a string.
   */
  public static String toTreeString(IPropertyPitProvider<?, ?, ?> pPitProvider)
  {
    return toTreeString(pPitProvider.getPit().getOwnProperty());
  }

  /**
   * Transforms given IProperty to a readable string.
   *
   * @param pProperty the starting point.
   * @return the subtree starting at the supplied IProperty as a string.
   */
  public static String toTreeString(IProperty<?, ?> pProperty)
  {
    StringBuilder stringBuilder = new StringBuilder();
    _buildTree(stringBuilder, pProperty, "");
    return stringBuilder.toString();
  }

  private static void _buildTree(StringBuilder pStr, IProperty<?, ?> pProperty, String pLevel)
  {
    if (pProperty == null)
      return;
    boolean isPitProvider = IPropertyPitProvider.class.isAssignableFrom(pProperty.getType());
    Object value = pProperty.getValue();

    pStr.append(pLevel);
    if (isPitProvider)
      pStr.append("/");
    else
      pStr.append(" ");
    pStr.append(pProperty.getName());
    if (!isPitProvider)
      pStr.append(" : ").append(value);
    pStr.append("\n");
    if (isPitProvider)
    {
      assert value != null;
      for (IProperty<?, ?> childProperty : ((IPropertyPitProvider<?, ?, ?>) value).getPit().getProperties())
        _buildTree(pStr, childProperty, pLevel + "\t");
    }
  }

}
