package de.adito.propertly.serialization.converter;

import net.java.sezpoz.Indexable;

import java.lang.annotation.*;

/**
 * @author j.boesl, 21.03.15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Indexable(type = IObjectConverter.class)
public @interface ConverterProvider
{
}
