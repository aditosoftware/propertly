package de.adito.propertly.test.core;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.PropertlyDebug;
import de.adito.propertly.core.common.PropertyPitEventAdapter;
import de.adito.propertly.core.common.exception.InaccessibleException;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.test.core.impl.ColoredPitProvider;
import de.adito.propertly.test.core.impl.PropertyTestChildren;
import de.adito.propertly.test.core.impl.TProperty;
import de.adito.propertly.test.core.impl.VerifyingHierarchy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.Set;
import java.util.function.Consumer;

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
    hierarchy.addStrongListener(new IPropertyPitEventListener<IPropertyPitProvider<?, ?, ?>, Object>()
    {
      @Override
      public void propertyValueWillBeChanged(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty,
                                             @Nullable Object pOldValue, @Nullable Object pNewValue,
                                             @NotNull Consumer<Runnable> pOnChanged, @NotNull Set<Object> pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyValueWillBeChanged", pOldValue, pNewValue, pProperty.getName(),
                pProperty, pAttributes);
        pOnChanged.accept(() -> _append(resultStringBuild, "hierarchy onValueChanged", pOldValue, pNewValue,
                                        pProperty.getName(), pProperty, pAttributes));
      }

      @Override
      public void propertyValueChanged(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty, @Nullable Object pOldValue,
                                       @Nullable Object pNewValue, @NotNull Set<Object> pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyValueChanged", pOldValue, pNewValue, pProperty.getName(), pProperty,
                pAttributes);
      }

      @Override
      public void propertyAdded(@NotNull IPropertyPitProvider<?, ?, ?> pSource,
                                @NotNull IPropertyDescription<IPropertyPitProvider<?, ?, ?>, Object> pPropertyDescription,
                                @NotNull Set<Object> pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyAdded", pSource, pPropertyDescription, pAttributes);
      }

      @Override
      public void propertyWillBeRemoved(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty,
                                        @NotNull Consumer<Runnable> pOnRemoved, @NotNull Set<Object> pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyWillBeRemoved", pProperty, pAttributes);
        pOnRemoved.accept(() -> _append(resultStringBuild, "hierarchy onPropertyRemoved", pProperty, pAttributes));
      }

      @Override
      public void propertyRemoved(@NotNull IPropertyPitProvider<?, ?, ?> pSource,
                                  @NotNull IPropertyDescription<IPropertyPitProvider<?, ?, ?>, Object> pPropertyDescription,
                                  @NotNull Set<Object> pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyRemoved", pSource, pPropertyDescription, pAttributes);
      }

      @Override
      public void propertyNameChanged(@NotNull IProperty<IPropertyPitProvider<?, ?, ?>, Object> pProperty, @NotNull String pOldName,
                                      @NotNull String pNewName, @NotNull Set<Object> pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyNameChanged", pOldName, pNewName, pProperty, pAttributes);
      }

      @Override
      public void propertyOrderWillBeChanged(@NotNull IPropertyPitProvider<?, ?, ?> pSource, @NotNull Consumer<Runnable> pOnChanged,
                                             @NotNull Set<Object> pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyOrderWillBeChanged", pSource, _getChildNames(pSource), pAttributes);
        pOnChanged.accept(() -> _append(resultStringBuild, "hierarchy onOrderChanged", pSource, _getChildNames(pSource), pAttributes));
      }

      @Override
      public void propertyOrderChanged(@NotNull IPropertyPitProvider<?, ?, ?> pSource, @NotNull Set<Object> pAttributes)
      {
        _append(resultStringBuild, "hierarchy propertyOrderChanged", pSource, _getChildNames(pSource), pAttributes);
      }
    });
    TProperty tProperty = hierarchy.getValue();
    PropertyTestChildren children = tProperty.setCHILD(new PropertyTestChildren());
    tProperty = children.addProperty("DynamicChildProperty", new TProperty()).getValue();
    tProperty.getPit().addStrongListener(new PropertyPitEventAdapter<TProperty, Object>()
    {
      @Override
      public void propertyValueChanged(@NotNull IProperty pProperty, Object pOldValue, Object pNewValue, @NotNull Set pAttributes)
      {
        _append(resultStringBuild, "tProperty propertyValueChanged", pProperty, pAttributes);
      }
    });
    children = tProperty.setCHILD(new PropertyTestChildren());
    children.addStrongListener(new PropertyPitEventAdapter<PropertyTestChildren, Object>()
    {
      @Override
      public void propertyAdded(@NotNull PropertyTestChildren pSource, @NotNull IPropertyDescription<PropertyTestChildren, Object> pPropertyDescription,
                                @NotNull Set<Object> pAttributes)
      {
        _append(resultStringBuild, "tProperty propertyAdded", pPropertyDescription, pAttributes);
      }
    });

    tProperty.getPropertyX().setValue(123, "attribute1");
    tProperty.setFF(new Dimension(123, 456));

    IProperty<PropertyTestChildren, Color> color3Property = children.addProperty(Color.class, "color3");
    children.addProperty(Color.class, "color1").setValue(Color.BLACK);
    children.addProperty(Color.class, "color4").setValue(Color.MAGENTA);
    children.addProperty(Color.class, "color2").setValue(Color.RED);

    children.reorder((o1, o2) -> o1.getName().compareTo(o2.getName()));

    children.removeProperty(1);
    children.removeProperty(color3Property);

    children.getProperty(0).rename("black");

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


    String expected = "hierarchy propertyValueWillBeChanged: null, IndexedMutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, null), []\n" +
        "hierarchy propertyValueChanged: null, IndexedMutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, IndexedMutablePropertyPit), []\n" +
        "hierarchy onValueChanged: null, IndexedMutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, IndexedMutablePropertyPit), []\n" +
        "hierarchy propertyAdded: IndexedMutablePropertyPit, description(DynamicChildProperty, class de.adito.propertly.test.core.impl.TProperty), []\n" +
        "hierarchy propertyValueWillBeChanged: null, PropertyPit, DynamicChildProperty, property(DynamicChildProperty, TProperty, null), []\n" +
        "hierarchy propertyValueChanged: null, PropertyPit, DynamicChildProperty, property(DynamicChildProperty, TProperty, PropertyPit), []\n" +
        "hierarchy onValueChanged: null, PropertyPit, DynamicChildProperty, property(DynamicChildProperty, TProperty, PropertyPit), []\n" +
        "hierarchy propertyValueWillBeChanged: null, IndexedMutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, null), []\n" +
        "hierarchy propertyValueChanged: null, IndexedMutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, IndexedMutablePropertyPit), []\n" +
        "tProperty propertyValueChanged: property(CHILD, PropertyTestChildren, IndexedMutablePropertyPit), []\n" +
        "hierarchy onValueChanged: null, IndexedMutablePropertyPit, CHILD, property(CHILD, PropertyTestChildren, IndexedMutablePropertyPit), []\n" +
        "hierarchy propertyValueWillBeChanged: null, 123, X, property(X, Integer, null), [attribute1]\n" +
        "hierarchy propertyValueChanged: null, 123, X, property(X, Integer, 123), [attribute1]\n" +
        "tProperty propertyValueChanged: property(X, Integer, 123), [attribute1]\n" +
        "hierarchy onValueChanged: null, 123, X, property(X, Integer, 123), [attribute1]\n" +
        "hierarchy propertyValueWillBeChanged: null, java.awt.Dimension[width=123,height=456], FF, property(FF, Dimension, null), []\n" +
        "hierarchy propertyValueChanged: null, java.awt.Dimension[width=123,height=456], FF, property(FF, Dimension, java.awt.Dimension[width=123,height=456]), []\n" +
        "tProperty propertyValueChanged: property(FF, Dimension, java.awt.Dimension[width=123,height=456]), []\n" +
        "hierarchy onValueChanged: null, java.awt.Dimension[width=123,height=456], FF, property(FF, Dimension, java.awt.Dimension[width=123,height=456]), []\n" +
        "hierarchy propertyAdded: IndexedMutablePropertyPit, description(color3, class java.awt.Color), []\n" +
        "tProperty propertyAdded: description(color3, class java.awt.Color), []\n" +
        "hierarchy propertyAdded: IndexedMutablePropertyPit, description(color1, class java.awt.Color), []\n" +
        "tProperty propertyAdded: description(color1, class java.awt.Color), []\n" +
        "hierarchy propertyValueWillBeChanged: null, java.awt.Color[r=0,g=0,b=0], color1, property(color1, Color, null), []\n" +
        "hierarchy propertyValueChanged: null, java.awt.Color[r=0,g=0,b=0], color1, property(color1, Color, java.awt.Color[r=0,g=0,b=0]), []\n" +
        "hierarchy onValueChanged: null, java.awt.Color[r=0,g=0,b=0], color1, property(color1, Color, java.awt.Color[r=0,g=0,b=0]), []\n" +
        "hierarchy propertyAdded: IndexedMutablePropertyPit, description(color4, class java.awt.Color), []\n" +
        "tProperty propertyAdded: description(color4, class java.awt.Color), []\n" +
        "hierarchy propertyValueWillBeChanged: null, java.awt.Color[r=255,g=0,b=255], color4, property(color4, Color, null), []\n" +
        "hierarchy propertyValueChanged: null, java.awt.Color[r=255,g=0,b=255], color4, property(color4, Color, java.awt.Color[r=255,g=0,b=255]), []\n" +
        "hierarchy onValueChanged: null, java.awt.Color[r=255,g=0,b=255], color4, property(color4, Color, java.awt.Color[r=255,g=0,b=255]), []\n" +
        "hierarchy propertyAdded: IndexedMutablePropertyPit, description(color2, class java.awt.Color), []\n" +
        "tProperty propertyAdded: description(color2, class java.awt.Color), []\n" +
        "hierarchy propertyValueWillBeChanged: null, java.awt.Color[r=255,g=0,b=0], color2, property(color2, Color, null), []\n" +
        "hierarchy propertyValueChanged: null, java.awt.Color[r=255,g=0,b=0], color2, property(color2, Color, java.awt.Color[r=255,g=0,b=0]), []\n" +
        "hierarchy onValueChanged: null, java.awt.Color[r=255,g=0,b=0], color2, property(color2, Color, java.awt.Color[r=255,g=0,b=0]), []\n" +
        "hierarchy propertyOrderWillBeChanged: IndexedMutablePropertyPit, [color3, color1, color4, color2], []\n" +
        "hierarchy propertyOrderChanged: IndexedMutablePropertyPit, [color1, color2, color3, color4], []\n" +
        "hierarchy onOrderChanged: IndexedMutablePropertyPit, [color1, color2, color3, color4], []\n" +
        "hierarchy propertyWillBeRemoved: property(color2, Color, java.awt.Color[r=255,g=0,b=0]), []\n" +
        "hierarchy propertyRemoved: IndexedMutablePropertyPit, description(color2, class java.awt.Color), []\n" +
        "hierarchy onPropertyRemoved: property(color2, Color, <invalid>), []\n" +
        "hierarchy propertyWillBeRemoved: property(color3, Color, null), []\n" +
        "hierarchy propertyRemoved: IndexedMutablePropertyPit, description(color3, class java.awt.Color), []\n" +
        "hierarchy onPropertyRemoved: property(color3, Color, <invalid>), []\n" +
        "hierarchy propertyNameChanged: color1, black, property(black, Color, java.awt.Color[r=0,g=0,b=0]), []\n" +
        "child parent: PropertyPit\n" +
        "tProperty child property: property(black, Color, java.awt.Color[r=0,g=0,b=0])\n" +
        "tProperty child property: property(color4, Color, java.awt.Color[r=255,g=0,b=255])\n" +
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
        "\t\t\t\t black : java.awt.Color[r=0,g=0,b=0]\n" +
        "\t\t\t\t color4 : java.awt.Color[r=255,g=0,b=255]\n" +
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

  private static String _getChildNames(IPropertyPitProvider<?, ?, ?> pPpp)
  {
    return "[" + pPpp.getPit().getProperties().stream()
        .map(IProperty::getName)
        .reduce((pS, pS2) -> pS + ", " + pS2).orElse("") + "]";
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
        r += "property(" + _toString(p.getName(), p.getType().getSimpleName(), p.isValid() ? _toString(p.getValue()) : "<invalid>") + ")";
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
