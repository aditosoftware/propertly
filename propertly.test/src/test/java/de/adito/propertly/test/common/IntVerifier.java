package de.adito.propertly.test.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author PaL
 *         Date: 09.02.13
 *         Time: 20:08
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntVerifier
{

  int minValue() default Integer.MIN_VALUE;

  int maxValue() default Integer.MAX_VALUE;


}
