package de.adito.propertly.serialization.structuredescription;

import de.adito.propertly.core.spi.IPropertyPitProvider;

import java.util.Map;

/**
 * @author j.boesl, 06.03.15
 */
public interface IDescriptionProvider
{

  String getSerializationDescription(Map<Class<? extends IPropertyPitProvider>, IPPPDescription> pStructure);

}
