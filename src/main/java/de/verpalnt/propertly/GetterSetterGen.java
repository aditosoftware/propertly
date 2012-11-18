package de.verpalnt.propertly;


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

  public static void run(IPropertyPit<?> pPit)
  {
    for (IPropertyDescription<?, ?> prop : pPit.getProperties())
    {
      System.out.println("public IProperty<" + prop.getParentType().getSimpleName() + ", " +
          prop.getType().getSimpleName() + "> getProperty" + Util.capitalize(prop.getName()) +
          "(){return pit.getProperty(" + prop.getName().toUpperCase() + ");}");

      System.out.println("public " + prop.getType().getSimpleName() + " get" + Util.capitalize(prop.getName()) +
          "(){return pit.getValue(" + prop.getName().toUpperCase() + ");}");

      System.out.println("public void set" + Util.capitalize(prop.getName()) + "(" + prop.getType().getSimpleName() +
          " p" + Util.capitalize(prop.getName()) + "){pit.setValue(" + prop.getName().toUpperCase() + ", p" +
          Util.capitalize(prop.getName()) + ");}");
    }

  }

}
