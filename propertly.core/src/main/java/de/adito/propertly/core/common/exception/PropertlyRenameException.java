package de.adito.propertly.core.common.exception;

import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.IProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Occurs when renaming failed.
 *
 * @author j.boesl, 16.11.14
 */
public class PropertlyRenameException extends RuntimeException
{

  private IProperty<?, ?> property;
  private String name;
  private Set<Object> attributes;

  public PropertlyRenameException(@NotNull IProperty<?, ?> pProperty, @NotNull String pName, @Nullable Object... pAttributes)
  {
    this(null, pProperty, pName, pAttributes);
  }

  public PropertlyRenameException(@Nullable Throwable cause, @NotNull IProperty<?, ?> pProperty, @NotNull String pName, @Nullable Object... pAttributes)
  {
    super(cause);
    property = pProperty;
    name = pName;
    attributes = PropertlyUtility.toNonnullSet(pAttributes);
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

  /**
   * @return additional attributes describing the failed rename request.
   */
  public Set<Object> getAttributes()
  {
    return attributes;
  }
}
