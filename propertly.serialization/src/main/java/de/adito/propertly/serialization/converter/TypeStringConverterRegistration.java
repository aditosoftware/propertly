package de.adito.propertly.serialization.converter;

import java.lang.annotation.*;

/**
 * @author j.boesl, 12.03.15
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeStringConverterRegistration
{
  String value();
}
