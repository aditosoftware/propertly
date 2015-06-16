package de.adito.propertly.core.common.path;

import de.adito.propertly.core.spi.*;

import javax.annotation.*;
import java.util.List;

/**
 * A path object describing the path from the root to a property.
 *
 * @author j.boesl, 11.05.15
 */
public interface IPropertyPath
{

  @Nullable
  IProperty<?, ?> find(IHierarchy<?> pHierarchy);

  /**
   * @return path as elements.
   */
  @Nonnull
  List<String> getPathElements();

  /**
   * @return path as string separated by slashes.
   */
  @Nonnull
  String asString();

}
