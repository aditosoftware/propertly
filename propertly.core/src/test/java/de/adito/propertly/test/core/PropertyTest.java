package de.adito.propertly.test.core;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.*;
import de.adito.propertly.core.common.exception.InaccessibleException;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.test.core.impl.ColoredPitProvider;
import de.adito.propertly.test.core.impl.PropertyTestChildren;
import de.adito.propertly.test.core.impl.TProperty;
import de.adito.propertly.test.core.impl.VerifyingHierarchy;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * @author PaL
 *         Date: 20.08.12
 *         Time: 00:55
 */
public class PropertyTest
{

  @Test
  public void simpleTest()
  {
    final StringBuilder resultStringBuild = new StringBuilder();

    IHierarchy<TProperty> hierarchy = new VerifyingHierarchy<TProperty>(new Hierarchy<TProperty>("root", new TProperty()));
    hierarchy.addStrongListener(new IPropertyPitEventListener()
    {
      @Override
      public void propertyChanged(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        _append(resultStringBuild, "hierarchy propertyChanged", pOldValue, pNewValue, pProperty.getName(), pProperty);
      }

      @Override
      public void propertyAdded(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        _append(resultStringBuild, "hierarchy propertyAdded", pSource, pPropertyDescription);
      }

      @Override
      public void propertyWillBeRemoved(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        _append(resultStringBuild, "hierarchy propertyWillBeRemoved", pSource, pPropertyDescription);
      }

      @Override
      public void propertyRemoved(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        _append(resultStringBuild, "hierarchy propertyRemoved", pSource, pPropertyDescription);
      }
    });
    TProperty tProperty = hierarchy.getValue();
    //GetterSetterGen.run(tProperty);
    tProperty.getPit().addStrongListener(new PropertyPitEventAdapter()
    {
      @Override
      public void propertyChanged(IProperty pProperty, Object pOldValue, Object pNewValue)
      {
        _append(resultStringBuild, "tProperty propertyChanged", pProperty);
      }
    });
    PropertyTestChildren children = tProperty.setCHILD(new PropertyTestChildren());
    children.addStrongListener(new PropertyPitEventAdapter()
    {
      @Override
      public void propertyAdded(IPropertyPitProvider pSource, IPropertyDescription pPropertyDescription)
      {
        _append(resultStringBuild, "tProperty propertyAdded", pPropertyDescription);
      }
    });

    tProperty.setX(123);
    tProperty.setFF(new Dimension(123, 456));

    children.addProperty(Color.class, "color1", null).setValue(Color.BLACK);
    children.addProperty(Color.class, "color2", null).setValue(Color.RED);

    _append(resultStringBuild, "child parent", tProperty.getCHILD().getParent());
    for (IProperty property : tProperty.getCHILD())
      _append(resultStringBuild, "tProperty child property", property);

    _append(resultStringBuild, "tProperty parent", tProperty.getPit().getParent());
    for (IProperty property : tProperty.getPit())
      _append(resultStringBuild, "tProperty property", property);


    Exception ex = null;
    try
    {
      tProperty.setX(-1);
    }
    catch (Exception e)
    {
      ex = e;
    }
    Assert.assertNotNull(ex);


    String expected = "hierarchy propertyChanged: null, MutablePropertyPit, root, property(root, IPropertyPitProvider, PropertyPit)\n" +
        "hierarchy propertyChanged: null, MutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "tProperty propertyChanged: property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "hierarchy propertyChanged: null, 123, root, property(root, IPropertyPitProvider, PropertyPit)\n" +
        "hierarchy propertyChanged: null, 123, X, property(X, Integer, 123)\n" +
        "tProperty propertyChanged: property(X, Integer, 123)\n" +
        "hierarchy propertyChanged: null, java.awt.Dimension[width=123,height=456], root, property(root, IPropertyPitProvider, PropertyPit)\n" +
        "hierarchy propertyChanged: null, java.awt.Dimension[width=123,height=456], FF, property(FF, Dimension, java.awt.Dimension[width=123,height=456])\n" +
        "tProperty propertyChanged: property(FF, Dimension, java.awt.Dimension[width=123,height=456])\n" +
        "hierarchy propertyAdded: MutablePropertyPit, description(CHILD, class de.adito.propertly.test.core.impl.PropertyTestChildren)\n" +
        "tProperty propertyAdded: description(color1, class java.awt.Color)\n" +
        "hierarchy propertyChanged: null, java.awt.Color[r=0,g=0,b=0], CHILD, property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "tProperty propertyChanged: property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "hierarchy propertyChanged: null, java.awt.Color[r=0,g=0,b=0], color1, property(color1, Color, java.awt.Color[r=0,g=0,b=0])\n" +
        "hierarchy propertyAdded: MutablePropertyPit, description(CHILD, class de.adito.propertly.test.core.impl.PropertyTestChildren)\n" +
        "tProperty propertyAdded: description(color2, class java.awt.Color)\n" +
        "hierarchy propertyChanged: null, java.awt.Color[r=255,g=0,b=0], CHILD, property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "tProperty propertyChanged: property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "hierarchy propertyChanged: null, java.awt.Color[r=255,g=0,b=0], color2, property(color2, Color, java.awt.Color[r=255,g=0,b=0])\n" +
        "child parent: PropertyPit\n" +
        "tProperty child property: property(color1, Color, java.awt.Color[r=0,g=0,b=0])\n" +
        "tProperty child property: property(color2, Color, java.awt.Color[r=255,g=0,b=0])\n" +
        "tProperty parent: null\n" +
        "tProperty property: property(X, Integer, 123)\n" +
        "tProperty property: property(Y, Integer, null)\n" +
        "tProperty property: property(FF, Dimension, java.awt.Dimension[width=123,height=456])\n" +
        "tProperty property: property(MAP, Map, null)\n" +
        "tProperty property: property(CHILD, PropertyTestChildren, MutablePropertyPit)\n" +
        "tProperty property: property(WIDTH, Integer, null)\n" +
        "tProperty property: property(HEIGHT, Integer, null)";

    Assert.assertEquals(expected,
        resultStringBuild.toString());

    expected = "/root\n" +
        "\t X : 123\n" +
        "\t Y : null\n" +
        "\t FF : java.awt.Dimension[width=123,height=456]\n" +
        "\t MAP : null\n" +
        "\t/CHILD\n" +
        "\t\t color1 : java.awt.Color[r=0,g=0,b=0]\n" +
        "\t\t color2 : java.awt.Color[r=255,g=0,b=0]\n" +
        "\t WIDTH : null\n" +
        "\t HEIGHT : null\n";

    Assert.assertEquals(expected,
                        PropertlyDebug.toTreeString(hierarchy));
  }

  private static void _append(StringBuilder pStrBuilder, String pEvent, Object... pAdd)
  {
    if (pStrBuilder.length() != 0)
      pStrBuilder.append("\n");
    pStrBuilder.append(pEvent).append(": ").append(_toString(pAdd));
  }

  private static String _toString(Object... pObj)
  {
    String r = "";
    for (Object o : pObj)
    {
      if (!r.isEmpty())
        r += ", ";

      if (o instanceof IPropertyPitProvider)
      {
        IPropertyPitProvider p = (IPropertyPitProvider) o;
        r += p.getPit().getClass().getSimpleName();
      }
      else if (o instanceof IProperty)
      {
        IProperty p = (IProperty) o;
        r += "property(" + _toString(p.getName(), p.getType().getSimpleName(), _toString(p.getValue())) + ")";
      }
      else if (o instanceof IPropertyDescription)
      {
        IPropertyDescription p = (IPropertyDescription) o;
        r += "description(" + _toString(p.getName(), p.getType() + ")");
      }
      else
        r += o;
    }
    return r;
  }

  @Test
  public void readWriteTest()
  {
    IHierarchy<ColoredPitProvider> hierarchy = new VerifyingHierarchy<ColoredPitProvider>(new Hierarchy<ColoredPitProvider>("root", new ColoredPitProvider()));
    IPropertyPit<IPropertyPitProvider, ColoredPitProvider, Color> pit = hierarchy.getValue().getPit();


    pit.setValue(ColoredPitProvider.DEFAULT_COLOR, Color.MAGENTA);
    Assert.assertEquals(pit.getValue(ColoredPitProvider.DEFAULT_COLOR), Color.MAGENTA);


    InaccessibleException ex = null;
    try
    {
      pit.setValue(ColoredPitProvider.READ_ONLY_COLOR, Color.CYAN);
    }
    catch (InaccessibleException e)
    {
      ex = e;
    }
    Assert.assertNotNull(ex);
    ex = null;
    Assert.assertEquals(pit.getValue(ColoredPitProvider.READ_ONLY_COLOR), null);


    pit.setValue(ColoredPitProvider.WRITE_ONLE_COLOR, Color.YELLOW);
    try
    {
      Assert.assertEquals(pit.getValue(ColoredPitProvider.WRITE_ONLE_COLOR), null);
    }
    catch (InaccessibleException e)
    {
      ex = e;
    }
    Assert.assertNotNull(ex);
    ex = null;


    try
    {
      pit.setValue(ColoredPitProvider.INACCESSIBLE_COLOR, Color.GREEN);
    }
    catch (InaccessibleException e)
    {
      ex = e;
    }
    Assert.assertNotNull(ex);
    ex = null;
    try
    {
      Assert.assertEquals(pit.getValue(ColoredPitProvider.INACCESSIBLE_COLOR), null);
    }
    catch (InaccessibleException e)
    {
      ex = e;
    }
    Assert.assertNotNull(ex);
  }

}
