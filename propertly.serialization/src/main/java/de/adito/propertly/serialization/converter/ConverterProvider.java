package de.adito.propertly.serialization.converter;

import de.adito.picoservice.PicoService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author j.boesl, 21.03.15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PicoService
public @interface ConverterProvider
{
}
