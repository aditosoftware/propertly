package de.adito.propertly.serialization.converter;

import de.adito.picoservice.PicoService;

import java.lang.annotation.*;

/**
 * @author j.boesl, 12.03.15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PicoService
public @interface ConverterSubTypeProvider
{
  String value();
}
