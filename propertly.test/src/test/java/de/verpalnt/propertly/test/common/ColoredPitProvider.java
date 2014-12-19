package de.verpalnt.propertly.test.common;

import de.verpalnt.propertly.core.api.*;
import de.verpalnt.propertly.core.common.PD;

import java.awt.*;

/**
 * @author j.boesl, 10.12.14
 */
public class ColoredPitProvider implements IPropertyPitProvider<IPropertyPitProvider, ColoredPitProvider, Color>
{

  @Override
  public IPropertyPit<IPropertyPitProvider, ColoredPitProvider, Color> getPit()
  {
    Class<ColoredPitProvider> coloredPitProviderClass = ColoredPitProvider.class;
    return null;
  }

  IPropertyDescription<ColoredPitProvider, Color> COLOR = PD.create(ColoredPitProvider.class);

}
