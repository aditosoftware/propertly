package de.adito.propertly.core.common;

import de.adito.propertly.core.spi.*;

import java.util.*;

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
    if (isPitProvider && value == null)
      return;

    String valueStringRep;
    if (pProperty.getType().isArray())
    {
      // We need to wrap the value in an array, so that we can use "deepToString" for
      // nested arrays inside our "value", if it is an array too
      valueStringRep = Arrays.deepToString(new Object[]{value});
      if (valueStringRep.length() >= 2)
        // cut off the first and last bracket, because this comes from our wrapping
        valueStringRep = valueStringRep.substring(1, valueStringRep.length() - 1);
    }
    else
      // fallback to default toString() handling
      valueStringRep = String.valueOf(value);

    pStr.append(pLevel);
    if (isPitProvider)
      pStr.append("/");
    else
      pStr.append(" ");
    pStr.append(pProperty.getName());
    if (!isPitProvider)
      pStr.append(" : ").append(valueStringRep);
    pStr.append("\n");
    if (isPitProvider)
    {
      for (IProperty<?, ?> childProperty : ((IPropertyPitProvider<?, ?, ?>) value).getPit().getProperties())
        _buildTree(pStr, childProperty, pLevel + "\t");
    }
  }

}
