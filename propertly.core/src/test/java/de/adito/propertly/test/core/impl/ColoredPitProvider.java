package de.adito.propertly.test.core.impl;

import de.adito.propertly.core.api.PitFactory;
import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPit;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author j.boesl, 10.12.14
 */
public class ColoredPitProvider implements IPropertyPitProvider<IPropertyPitProvider, ColoredPitProvider, Color>
{

  private IPropertyPit<IPropertyPitProvider, ColoredPitProvider, Color> pit = PitFactory.getInstance().create(this);

  @NotNull
  @Override
  public IPropertyPit<IPropertyPitProvider, ColoredPitProvider, Color> getPit()
  {
    return pit;
  }

  public static final IPropertyDescription<ColoredPitProvider, Color> DEFAULT_COLOR = PD.create(ColoredPitProvider.class);

  @AccessModifier(canWrite = false)
  public static final IPropertyDescription<ColoredPitProvider, Color> READ_ONLY_COLOR = PD.create(ColoredPitProvider.class);

  @AccessModifier(canRead = false)
  public static final IPropertyDescription<ColoredPitProvider, Color> WRITE_ONLE_COLOR = PD.create(ColoredPitProvider.class);

  @AccessModifier(canRead = false, canWrite = false)
  public static final IPropertyDescription<ColoredPitProvider, Color> INACCESSIBLE_COLOR = PD.create(ColoredPitProvider.class);

}
