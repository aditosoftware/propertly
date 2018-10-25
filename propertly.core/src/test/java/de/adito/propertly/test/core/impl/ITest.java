package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;

/**
 * @author PaL
 *         Date: 14.11.12
 *         Time: 00:41
 */
public interface ITest<S extends ITest<S>> extends IPropertyPitProvider<IPropertyPitProvider, S, Object>
{

  IPropertyDescription<ITest, PropertyTestChildren> CHILD = PD.create(ITest.class);

}
