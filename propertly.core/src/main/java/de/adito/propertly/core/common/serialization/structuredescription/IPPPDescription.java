package de.adito.propertly.core.common.serialization.structuredescription;

import de.adito.propertly.core.spi.*;

import java.util.Set;

/**
 * @author j.boesl, 09.03.15
 */
public interface IPPPDescription
{

  Class<? extends IPropertyPitProvider> getType();

  Class getAllowedSubType();

  boolean isMutable();

  Set<IPropertyDescription> getPropertyDescriptions();

}
