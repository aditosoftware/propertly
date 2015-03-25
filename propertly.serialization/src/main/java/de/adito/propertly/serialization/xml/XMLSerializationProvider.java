package de.adito.propertly.serialization.xml;

import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.serialization.ISerializationProvider;
import de.adito.propertly.serialization.converter.ConverterRegistry;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.annotation.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author j.boesl, 28.02.15
 */
public class XMLSerializationProvider implements ISerializationProvider<Document, Element>
{

  private final ConverterRegistry converterRegistry;


  public XMLSerializationProvider()
  {
    this(new ConverterRegistry());
  }

  public XMLSerializationProvider(ConverterRegistry pConverterRegistry)
  {
    converterRegistry = pConverterRegistry;
  }

  public static String toString(Document pDocument) throws TransformerException
  {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    StreamResult result = new StreamResult(new StringWriter());
    DOMSource source = new DOMSource(pDocument);
    transformer.transform(source, result);
    return result.getWriter().toString();
  }

  public static Document toDocument(String pXmlString) throws IOException, SAXException, ParserConfigurationException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(new ByteArrayInputStream(pXmlString.getBytes()));
  }

  @Nonnull
  @Override
  public Document serializeRootNode(
      @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
      @Nonnull IChildRunner<Element> pChildRunner)
  {
    try
    {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document document = docBuilder.newDocument();
      Element element = document.createElement(converterRegistry.typeToString(pPropertyType));
      element.setAttribute("name", pName);
      document.appendChild(element);
      pChildRunner.run(element);
      return document;
    }
    catch (ParserConfigurationException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void serializeFixedNode(
      @Nonnull Element pParentOutputData, @Nonnull String pName, @Nonnull IChildRunner<Element> pChildRunner)
  {
    final Element element = pParentOutputData.getOwnerDocument().createElement(pName);
    pParentOutputData.appendChild(element);
    pChildRunner.run(element);
  }

  @Override
  public void serializeFixedNode(
      @Nonnull Element pParentOutputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pType,
      @Nonnull IChildRunner<Element> pChildRunner)
  {
    final Element element = pParentOutputData.getOwnerDocument().createElement(pName);
    pParentOutputData.appendChild(element);
    element.setAttribute("type", converterRegistry.typeToString(pType));
    pChildRunner.run(element);
  }

  @Override
  public void serializeDynamicNode(
      @Nonnull Element pParentOutputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
      @Nullable List<? extends Annotation> pAnnotations, @Nonnull IChildRunner<Element> pChildRunner)
  {
    Element element = pParentOutputData.getOwnerDocument().createElement(converterRegistry.typeToString(pPropertyType));
    pParentOutputData.appendChild(element);
    element.setAttribute("name", pName);
    if (pAnnotations != null && !pAnnotations.isEmpty())
      element.setAttribute("annotations", pAnnotations.toString());
    pChildRunner.run(element);
  }

  @Override
  public void serializeDynamicNode(
      @Nonnull Element pParentOutputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
      @Nonnull Class<? extends IPropertyPitProvider> pType, @Nullable List<? extends Annotation> pAnnotations,
      @Nonnull IChildRunner<Element> pChildRunner)
  {
    Element element = pParentOutputData.getOwnerDocument().createElement(converterRegistry.typeToString(pPropertyType));
    pParentOutputData.appendChild(element);
    element.setAttribute("name", pName);
    element.setAttribute("type", converterRegistry.typeToString(pPropertyType));
    if (pAnnotations != null && !pAnnotations.isEmpty())
      element.setAttribute("annotations", pAnnotations.toString());
    pChildRunner.run(element);
  }

  @Override
  public void serializeFixedValue(
      @Nonnull Element pParentOutputData, @Nonnull String pName, @Nonnull Object pValue)
  {
    final Element element = pParentOutputData.getOwnerDocument().createElement(pName);
    pParentOutputData.appendChild(element);
    element.setTextContent(converterRegistry.findObjectStringConverter(pValue.getClass()).valueToString(pValue));
  }

  @Override
  public <V> void serializeDynamicValue(
      @Nonnull Element pParentOutputData, @Nonnull String pName, @Nonnull Class<? super V> pPropertyType,
      @Nullable V pValue, @Nullable List<? extends Annotation> pAnnotations)
  {
    final Element element = pParentOutputData.getOwnerDocument().createElement(converterRegistry.typeToString(pPropertyType));
    pParentOutputData.appendChild(element);
    element.setAttribute("name", pName);
    if (pValue != null && pValue.getClass() != pPropertyType)
      element.setAttribute("type", converterRegistry.typeToString(pValue.getClass()));
    if (pAnnotations != null && !pAnnotations.isEmpty())
      element.setAttribute("annotations", pAnnotations.toString());
    if (pValue != null)
      element.setTextContent(converterRegistry.findObjectStringConverter(pValue.getClass()).valueToString(pValue));
  }


  @Override
  public void deserializeRoot(@Nonnull Document pRootData, @Nonnull IChildAppender<Element> pChildAppender)
  {
    _deserialize(pRootData.getDocumentElement(), pChildAppender);
  }

  @Override
  public void deserializeChild(@Nonnull Element pInputData, @Nonnull IChildAppender<Element> pChildAppender)
  {
    NodeList childNodes = pInputData.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++)
    {
      Node node = childNodes.item(i);
      if (node instanceof Element)
      {
        Element e = (Element) node;
        _deserialize(e, pChildAppender);
      }
    }
  }

  private void _deserialize(@Nonnull Element pElement, @Nonnull IChildAppender<Element> pAppendChild)
  {
    String name;
    if (pElement.hasAttribute("name"))
      name = pElement.getAttribute("name");
    else
      name = pElement.getTagName();
    IChildDetail childDetail = pAppendChild.getChildDetail(name);
    switch (childDetail.getCategory())
    {
      case FIXED_NODE:
        if (pElement.hasAttribute("type"))
          pAppendChild.appendFixedNode(pElement, name, converterRegistry.findTypeStringConverter(childDetail.getType()).stringToType(pElement.getAttribute("type")));
        else
          pAppendChild.appendFixedNode(pElement, name);
        break;
      case FIXED_VALUE:
        pAppendChild.appendFixedValue(name, converterRegistry.findObjectStringConverter(childDetail.getType()).stringToValue(pElement.getTextContent(), childDetail.getType()));
        break;
      case DYNAMIC:
      default:
        String specificType = null;
        if (pElement.hasAttribute("type"))
          specificType = pElement.getAttribute("type");
        String type = pElement.getTagName();
        Class cls = converterRegistry.stringToType(type);
        if (IPropertyPitProvider.class.isAssignableFrom(cls))
        {
          if (specificType == null)
            pAppendChild.appendDynamicNode(pElement, name, (Class<IPropertyPitProvider>) cls, null);
          else
            pAppendChild.appendDynamicNode(pElement, name, (Class<IPropertyPitProvider>) cls, converterRegistry.stringToType(specificType), null);
        }
        else
        {
          if (specificType == null)
            specificType = type;
          Class specificCls = converterRegistry.stringToType(specificType);
          pAppendChild.appendDynamicValue(name, cls, converterRegistry.findObjectStringConverter(specificCls).stringToValue(pElement.getTextContent(), specificCls), null);
        }
        break;
    }
  }

}
