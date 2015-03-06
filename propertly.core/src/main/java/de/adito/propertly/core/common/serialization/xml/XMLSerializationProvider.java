package de.adito.propertly.core.common.serialization.xml;

import de.adito.propertly.core.common.serialization.ISerializationProvider;
import de.adito.propertly.core.common.serialization.converter.ConverterRegistry;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.w3c.dom.*;

import javax.annotation.*;
import javax.xml.parsers.*;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author j.boesl, 28.02.15
 */
public class XMLSerializationProvider implements ISerializationProvider<Element>
{

  private final ConverterRegistry converterRegistry;


  public XMLSerializationProvider() throws ParserConfigurationException
  {
    converterRegistry = new ConverterRegistry();
  }


  @Nonnull
  @Override
  public Element serializeFixedNode(
      @Nonnull Element pParentOutputData, @Nonnull String pName, @Nonnull IChildRunner<Element> pChildRunner)
  {
    final Element element = pParentOutputData.getOwnerDocument().createElement(pName);
    pParentOutputData.appendChild(element);
    pChildRunner.run(element);
    return pParentOutputData;
  }

  @Nonnull
  @Override
  public Element serializeFixedNode(
      @Nonnull Element pParentOutputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pType,
      @Nonnull IChildRunner<Element> pChildRunner)
  {
    final Element element = pParentOutputData.getOwnerDocument().createElement(pName);
    pParentOutputData.appendChild(element);
    element.setAttribute("type", converterRegistry.typeToString(pType));
    pChildRunner.run(element);
    return pParentOutputData;
  }

  @Nonnull
  @Override
  public Element serializeDynamicNode(
      @Nullable Element pParentOutputData, @Nonnull String pName, @Nonnull Class<? extends IPropertyPitProvider> pPropertyType,
      @Nullable List<? extends Annotation> pAnnotations, @Nonnull IChildRunner<Element> pChildRunner)
  {
    Element element;
    if (pParentOutputData == null)
    {
      try
      {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        pParentOutputData = document.createElement(converterRegistry.typeToString(pPropertyType));
        document.appendChild(pParentOutputData);
        element = pParentOutputData;
      }
      catch (ParserConfigurationException e)
      {
        throw new RuntimeException(e);
      }
    }
    else
    {
      element = pParentOutputData.getOwnerDocument().createElement(converterRegistry.typeToString(pPropertyType));
      pParentOutputData.appendChild(element);
    }
    element.setAttribute("name", pName);
    if (pAnnotations != null && !pAnnotations.isEmpty())
      element.setAttribute("annotations", pAnnotations.toString());
    pChildRunner.run(element);
    return pParentOutputData;
  }

  @Nonnull
  @Override
  public Element serializeDynamicNode(
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
    return pParentOutputData;
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
  public void deserialize(@Nonnull Element pInputData, @Nonnull IChildAppender<Element> pAppendChild)
  {
    if (pInputData.getOwnerDocument().getDocumentElement().equals(pInputData) && !pInputData.hasAttribute("visited"))
    {
      // workaround :/
      pInputData.setAttribute("visited", "true");
      _deserialize(pInputData, pAppendChild);
      pInputData.removeAttribute("visited");
    }
    else
    {
      NodeList childNodes = pInputData.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++)
      {
        Node node = childNodes.item(i);
        if (node instanceof Element)
        {
          Element e = (Element) node;
          _deserialize(e, pAppendChild);
        }
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
