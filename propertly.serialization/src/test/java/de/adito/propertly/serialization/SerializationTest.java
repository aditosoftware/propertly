package de.adito.propertly.serialization;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.PropertlyDebug;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.serialization.xml.XMLSerializationProvider;
import de.adito.propertly.test.core.impl.*;
import org.junit.*;
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
    IHierarchy<TProperty> hierarchy = new VerifyingHierarchy<TProperty>(new Hierarchy<TProperty>("root", new TProperty()));
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


    Serializer<Document> xmlSerializer = Serializer.create(new XMLSerializationProvider());
    Document document = xmlSerializer.serialize(hierarchy);
    deserialize = xmlSerializer.deserialize(document);

    Assert.assertEquals(PropertlyDebug.toTreeString(tProperty),
                        PropertlyDebug.toTreeString(deserialize));
  }

}
