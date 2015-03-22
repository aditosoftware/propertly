package de.adito.propertly.serialization.converter;

import net.java.sezpoz.Indexable;

import java.lang.annotation.*;

/**
 * @author j.boesl, 12.03.15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Indexable
public @interface ConverterSubTypeProvider
{
  String value();
}
