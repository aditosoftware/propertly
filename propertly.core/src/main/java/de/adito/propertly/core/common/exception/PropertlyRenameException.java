package de.adito.propertly.core.common.exception;

import de.adito.propertly.core.spi.IProperty;

import javax.annotation.Nonnull;

/**
 * Occurs when renaming failed.
 *
 * @author j.boesl, 16.11.14
 */
public class PropertlyRenameException extends RuntimeException
{

  private IProperty<?, ?> property;
  private String name;

  public PropertlyRenameException(@Nonnull IProperty<?, ?> pProperty, @Nonnull String pName)
  {
    this(null, pProperty, pName);
  }

  public PropertlyRenameException(Throwable cause, IProperty<?, ?> pProperty, String pName)
  {
    super(cause);
    property = pProperty;
    name = pName;
  }

  @Override
  public String getMessage()
  {
    return "failed renaming '" + property + "' to '" + name + "'.";
  }

  /**
   * @return the IProperty that was tried to rename.
   */
  public IProperty<?, ?> getProperty()
  {
    return property;
  }

  /**
   * @return the new name the IProperty should have been renamed to.
   */
  public String getName()
  {
    return name;
  }
}
