package de.adito.propertly.core.common.exception;

import de.adito.propertly.core.spi.IProperty;

import javax.annotation.Nonnull;

/**
 * @author j.boesl, 16.11.14
 */
public class PropertlyRenameException extends RuntimeException
{

  private IProperty<?, ?> property;
  private String name;

  public PropertlyRenameException(@Nonnull IProperty<?, ?> pProperty, @Nonnull String pName)
  {
    property = pProperty;
    name = pName;
  }

  @Override
  public String getMessage()
  {
    return "failed renaming '" + property + "' to '" + name + "'.";
  }

  public IProperty<?, ?> getProperty()
  {
    return property;
  }

  public String getName()
  {
    return name;
  }
}
