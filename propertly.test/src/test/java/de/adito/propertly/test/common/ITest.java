package de.adito.propertly.test.common;

import de.adito.propertly.core.api.IPropertyDescription;
import de.adito.propertly.core.api.IPropertyPitProvider;
import de.adito.propertly.core.common.PD;

/**
 * @author PaL
 *         Date: 14.11.12
 *         Time: 00:41
 */
public interface ITest<S extends ITest<S>> extends IPropertyPitProvider<IPropertyPitProvider, S, Object>
{

  IPropertyDescription<ITest, PropertyTestChildren> CHILD = PD.create(ITest.class);

}
