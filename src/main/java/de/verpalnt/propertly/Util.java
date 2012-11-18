package de.verpalnt.propertly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author PaL
 *         Date: 01.09.12
 *         Time: 16:45
 */
class Util
{

  static String capitalize(String pStr)
  {
    if (pStr == null || pStr.isEmpty())
      return pStr;
    return pStr.substring(0, 1).toUpperCase() + pStr.substring(1, pStr.length());
  }

  static List<Field> getAllFields(Class pCls)
  {
    List<Field> fields = new ArrayList<Field>();
    Collections.addAll(fields, pCls.getDeclaredFields());
    for (Class<?> c = pCls; c != null; c = c.getSuperclass())
      fields.addAll(Arrays.asList(c.getDeclaredFields()));
    for (Class<?> face : pCls.getInterfaces())
      Collections.addAll(fields, face.getDeclaredFields());
    return fields;
  }

}
