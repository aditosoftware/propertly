package de.adito.propertly.serialization;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.PropertlyDebug;
import de.adito.propertly.core.spi.IHierarchy;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.serialization.converter.ConverterRegistry;
import de.adito.propertly.serialization.converter.impl.EnumStringConverter;
import de.adito.propertly.serialization.converter.impl.TypePPPStringConverter;
import de.adito.propertly.serialization.xml.XMLSerializationProvider;
import de.adito.propertly.test.core.impl.PropertyTestChildren;
import de.adito.propertly.test.core.impl.TProperty;
import de.adito.propertly.test.core.impl.VerifyingHierarchy;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.util.Map;

/**
 * @author j.boesl, 27.02.15
 */
public class SerializationTest
{

  @Test
  public void simple() throws ParserConfigurationException, TransformerException
  {
    IHierarchy<TProperty> hierarchy = new VerifyingHierarchy<>(new Hierarchy<>("root", new TProperty()));
    TProperty tProperty = hierarchy.getValue();

    PropertyTestChildren children = tProperty.setCHILD(new PropertyTestChildren());

    tProperty.setX(123);
    tProperty.setFF(new Dimension(123, 456));

    children.addProperty(Color.class, "color1").setValue(Color.BLACK);
    children.addProperty(Color.class, "color2").setValue(Color.RED);
    children.addProperty(TProperty.class, "pppChild");

    Serializer<Map<String, Object>> mapSerializer = Serializer.create(new MapSerializationProvider());


    Map<String, Object> serialize = mapSerializer.serialize(hierarchy);
    IPropertyPitProvider deserialize = mapSerializer.deserialize(serialize);

    Assert.assertEquals(PropertlyDebug.toTreeString(tProperty),
                        PropertlyDebug.toTreeString(deserialize));


    Serializer<Document> xmlSerializer = Serializer.create(new XMLSerializationProvider(_getConverterRegistry()));
    Document document = xmlSerializer.serialize(hierarchy);
    deserialize = xmlSerializer.deserialize(document);

    Assert.assertEquals(PropertlyDebug.toTreeString(tProperty),
                        PropertlyDebug.toTreeString(deserialize));
  }

  private static ConverterRegistry _getConverterRegistry()
  {
    ConverterRegistry converterRegistry = new ConverterRegistry();

    TypePPPStringConverter typePPPStringConverter = new TypePPPStringConverter();
    typePPPStringConverter.register(TProperty.class, null);
    converterRegistry.register(typePPPStringConverter);

    EnumStringConverter enumStringConverter = new EnumStringConverter();
    converterRegistry.register(enumStringConverter);

    return converterRegistry;
  }

}
