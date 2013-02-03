package de.verpalnt.propertly.core.common;


import de.verpalnt.propertly.core.IPropertyDescription;
import de.verpalnt.propertly.core.IPropertyPitProvider;
import de.verpalnt.propertly.core.PropertyPit;

/**
 * @author PaL
 *         Date: 12.10.12
 *         Time: 23:10
 */
public class GetterSetterGen
{

  private GetterSetterGen()
  {
  }

  public static void run(PropertyPit<?> pPit)
  {
    run(pPit, null);
  }

  public static void run(IPropertyPitProvider<?> pPitProvider, String pPitName)
  {
    String pit = pPitName == null || pPitName.isEmpty() ? "" : pPitName + ".";
    for (IPropertyDescription<?, ?> prop : pPitProvider.getPit().getPropertyDescriptions())
    {
      System.out.println("public IProperty<" + prop.getParentType().getSimpleName() + ", " +
          prop.getType().getSimpleName() + "> getProperty" + capitalize(prop.getName()) +
          "(){return " + pit + "getProperty(" + prop.getName().toUpperCase() + ");}");

      System.out.println("public " + prop.getType().getSimpleName() + " get" + capitalize(prop.getName()) +
          "(){return " + pit + "getValue(" + prop.getName().toUpperCase() + ");}");

      System.out.println("public void set" + capitalize(prop.getName()) + "(" + prop.getType().getSimpleName() +
          " p" + capitalize(prop.getName()) + "){" + pit + "setValue(" + prop.getName().toUpperCase() + ", p" +
          capitalize(prop.getName()) + ");}");
    }
  }

  static String capitalize(String pStr)
  {
    if (pStr == null || pStr.isEmpty())
      return pStr;
    return pStr.substring(0, 1).toUpperCase() + pStr.substring(1, pStr.length());
  }


}
