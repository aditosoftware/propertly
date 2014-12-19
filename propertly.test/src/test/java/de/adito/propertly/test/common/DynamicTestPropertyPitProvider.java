package de.adito.propertly.test.common;

import de.adito.propertly.core.api.IPropertyPitProvider;
import de.adito.propertly.core.api.base.AbstractIndexedMutablePPP;

import java.awt.*;

/**
 * @author j.boesl, 02.12.14
 */
public class DynamicTestPropertyPitProvider extends AbstractIndexedMutablePPP<IPropertyPitProvider, DynamicTestPropertyPitProvider, Color>
{
  public DynamicTestPropertyPitProvider()
  {
    super(Color.class);
  }
}
