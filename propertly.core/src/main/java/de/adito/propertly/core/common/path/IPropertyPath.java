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

  /**
   * Tries to find an IProperty in the supplied IHierarchy for this path.
   *
   * @param pHierarchy the hierarchy object to search in.
   * @return the found property or <tt>null</tt> if none was found.
   */
  @Nullable
  IProperty<?, ?> find(IHierarchy<?> pHierarchy);

  /**
   * @return the parental IPropertyPath object.
   * @throws NoParentPathForRootException when called on the root path.
   */
  @Nonnull
  IPropertyPath getParent() throws NoParentPathForRootException;

  /**
   * @param pPath the path to test against.
   * @return whether given path <tt>pPath</tt> is a child path of this path.
   */
  boolean isParentOf(IPropertyPath pPath);

  /**
   * @param pName the child's name.
   * @return a new IPropertyPath for the child.
   */
  @Nonnull
  IPropertyPath getChild(String pName);

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
