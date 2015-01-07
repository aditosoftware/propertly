package de.adito.propertly.test.core.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author PaL
 *         Date: 07.01.15
 *         Time. 22:28
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessModifier
{

  boolean canRead() default true;

  boolean canWrite() default true;

}
