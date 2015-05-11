package de.adito.propertly.core.spi;

import java.util.List;

/**
 * A path object describing the path from the root to a property.
 *
 * @author j.boesl, 11.05.15
 */
public interface IPropertyPath
{

  /**
   * @return path as elements.
   */
  List<String> getPathElements();

  /**
   * @return path as string separated by slashes.
   */
  String asString();

}
