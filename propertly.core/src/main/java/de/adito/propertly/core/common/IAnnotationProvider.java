package de.adito.propertly.core.common;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * An common interface for something which supplies annotations.
 *
 * @author j.boesl, 09.12.14
 */
public interface IAnnotationProvider
{

  /**
   * @return an arbitrary list of annotations.
   */
  @Nonnull
  List<? extends Annotation> getAnnotations();

}
