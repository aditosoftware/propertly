package de.verpalnt.propertly.core;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author PaL
 *         Date: 29.09.11
 *         Time: 21:39
 */
public interface IPropertyDescription<S, T>
{

  Class<S> getParentType();

  Class<T> getType();

  String getName();

  List<? extends Annotation> getAnnotations();

}
