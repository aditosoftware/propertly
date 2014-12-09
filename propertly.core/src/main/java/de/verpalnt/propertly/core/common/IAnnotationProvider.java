package de.verpalnt.propertly.core.common;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author j.boesl, 09.12.14
 */
public interface IAnnotationProvider
{

  @Nonnull
  List<? extends Annotation> getAnnotations();

}
