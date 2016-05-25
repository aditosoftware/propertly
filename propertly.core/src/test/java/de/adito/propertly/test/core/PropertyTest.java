package de.adito.propertly.test.core;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.*;
import de.adito.propertly.core.common.exception.InaccessibleException;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.test.core.impl.*;
import org.junit.*;

import javax.annotation.*;
import java.awt.*;
import java.util.Set;

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

    Hierarchy<TProperty> sourceHierarchy = new Hierarchy<>("root", new TProperty());
    IHierarchy<TProperty> hierarchy = new VerifyingHierarchy<>(sourceHierarchy);
    hierarchy.addStrongListener(new IPropertyPitEventListener()
    {
      @Override
      public void propertyValueChanged(@Nonnull IProperty pProperty, @Nullable Object pOldValue, @Nullable Object pNewValue, @Nonnull Set pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyValueChanged", pOldValue, pNewValue, pProperty.getName(), pProperty);
      }

      @Override
      public void propertyAdded(@Nonnull IPropertyPitProvider pSource, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyAdded", pSource, pPropertyDescription);
      }

      @Override
      public void propertyWillBeRemoved(@Nonnull IProperty pProperty, @Nonnull Set pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyWillBeRemoved", pProperty);
      }

      @Override
      public void propertyRemoved(@Nonnull IPropertyPitProvider pSource, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyRemoved", pSource, pPropertyDescription);
      }

      @Override
      public void propertyNameChanged(@Nonnull IProperty pProperty, @Nonnull String pOldName, @Nonnull String pNewName, @Nonnull Set pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyNameChanged", pOldName, pNewName, pProperty.getName(), pProperty);
      }

      @Override
      public void propertyOrderChanged(@Nonnull IPropertyPitProvider pSource, @Nonnull Set pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyOrderChanged", pSource);
      }
    });
    TProperty tProperty = hierarchy.getValue();
    PropertyTestChildren children = tProperty.setCHILD(new PropertyTestChildren());
    tProperty = children.addProperty("DynamicChildProperty", new TProperty()).getValue();
    //GetterSetterGen.run(tProperty);
    tProperty.getPit().addStrongListener(new PropertyPitEventAdapter()
    {
      @Override
      public void propertyValueChanged(@Nonnull IProperty pProperty, Object pOldValue, Object pNewValue, @Nonnull Set pAttributes)
      {
        _append(resultStringBuild, "tProperty propertyValueChanged", pProperty);
      }
    });
    children = tProperty.setCHILD(new PropertyTestChildren());
    children.addStrongListener(new PropertyPitEventAdapter()
    {
      @Override
      public void propertyAdded(@Nonnull IPropertyPitProvider pSource, @Nonnull IPropertyDescription pPropertyDescription, @Nonnull Set pAttributes)
      {
        _append(resultStringBuild, "tProperty propertyAdded", pPropertyDescription);
      }
    });

    tProperty.setX(123);
    tProperty.setFF(new Dimension(123, 456));

    children.addProperty(Color.class, "color1").setValue(Color.BLACK);
    children.addProperty(Color.class, "color2").setValue(Color.RED);

    IProperty<PropertyTestChildren, Color> color3Property = children.addProperty(Color.class, "color3");
    children.removeProperty(1);
    children.removeProperty((IPropertyDescription) color3Property.getDescription());

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


    String expected = "hierarchy propertyValueChanged: null, IndexedMutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, IndexedMutablePropertyPit)\n" +
        "hierarchy propertyAdded: IndexedMutablePropertyPit, description(DynamicChildProperty, class de.adito.propertly.test.core.impl.TProperty)\n" +
        "hierarchy propertyValueChanged: null, PropertyPit, DynamicChildProperty, property(DynamicChildProperty, TProperty, PropertyPit)\n" +
        "hierarchy propertyValueChanged: null, IndexedMutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, IndexedMutablePropertyPit)\n" +
        "tProperty propertyValueChanged: property(CHILD, PropertyTestChildren, IndexedMutablePropertyPit)\n" +
        "hierarchy propertyValueChanged: null, 123, X, property(X, Integer, 123)\n" +
        "tProperty propertyValueChanged: property(X, Integer, 123)\n" +
        "hierarchy propertyValueChanged: null, java.awt.Dimension[width=123,height=456], FF, property(FF, Dimension, java.awt.Dimension[width=123,height=456])\n" +
        "tProperty propertyValueChanged: property(FF, Dimension, java.awt.Dimension[width=123,height=456])\n" +
        "hierarchy propertyAdded: IndexedMutablePropertyPit, description(color1, class java.awt.Color)\n" +
        "tProperty propertyAdded: description(color1, class java.awt.Color)\n" +
        "hierarchy propertyValueChanged: null, java.awt.Color[r=0,g=0,b=0], color1, property(color1, Color, java.awt.Color[r=0,g=0,b=0])\n" +
        "hierarchy propertyAdded: IndexedMutablePropertyPit, description(color2, class java.awt.Color)\n" +
        "tProperty propertyAdded: description(color2, class java.awt.Color)\n" +
        "hierarchy propertyValueChanged: null, java.awt.Color[r=255,g=0,b=0], color2, property(color2, Color, java.awt.Color[r=255,g=0,b=0])\n" +
        "hierarchy propertyAdded: IndexedMutablePropertyPit, description(color3, class java.awt.Color)\n" +
        "tProperty propertyAdded: description(color3, class java.awt.Color)\n" +
        "hierarchy propertyWillBeRemoved: property(color2, Color, java.awt.Color[r=255,g=0,b=0])\n" +
        "hierarchy propertyRemoved: IndexedMutablePropertyPit, description(color2, class java.awt.Color)\n" +
        "hierarchy propertyWillBeRemoved: property(color3, Color, null)\n" +
        "hierarchy propertyRemoved: IndexedMutablePropertyPit, description(color3, class java.awt.Color)\n" +
        "child parent: PropertyPit\n" +
        "tProperty child property: property(color1, Color, java.awt.Color[r=0,g=0,b=0])\n" +
        "tProperty parent: IndexedMutablePropertyPit\n" +
        "tProperty property: property(X, Integer, 123)\n" +
        "tProperty property: property(Y, Integer, null)\n" +
        "tProperty property: property(FF, Dimension, java.awt.Dimension[width=123,height=456])\n" +
        "tProperty property: property(MAP, Map, null)\n" +
        "tProperty property: property(CHILD, PropertyTestChildren, IndexedMutablePropertyPit)\n" +
        "tProperty property: property(WIDTH, Integer, null)\n" +
        "tProperty property: property(HEIGHT, Integer, null)\n" +
        "tProperty property: property(DESCRIPTION, String, null)";

    Assert.assertEquals(expected,
                        resultStringBuild.toString());

    expected = "/root\n" +
        "\t X : null\n" +
        "\t Y : null\n" +
        "\t FF : null\n" +
        "\t MAP : null\n" +
        "\t/CHILD\n" +
        "\t\t/DynamicChildProperty\n" +
        "\t\t\t X : 123\n" +
        "\t\t\t Y : null\n" +
        "\t\t\t FF : java.awt.Dimension[width=123,height=456]\n" +
        "\t\t\t MAP : null\n" +
        "\t\t\t/CHILD\n" +
        "\t\t\t\t color1 : java.awt.Color[r=0,g=0,b=0]\n" +
        "\t\t\t WIDTH : null\n" +
        "\t\t\t HEIGHT : null\n" +
        "\t\t\t DESCRIPTION : null\n" +
        "\t WIDTH : null\n" +
        "\t HEIGHT : null\n" +
        "\t DESCRIPTION : null\n";

    Assert.assertEquals(expected,
                        PropertlyDebug.toTreeString(hierarchy));

    Assert.assertEquals(expected,
                        PropertlyDebug.toTreeString(sourceHierarchy));
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
    IHierarchy<ColoredPitProvider> hierarchy = new VerifyingHierarchy<>(new Hierarchy<>("root", new ColoredPitProvider()));
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
