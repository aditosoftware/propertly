package de.verpalnt.propertly.core;

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

}
