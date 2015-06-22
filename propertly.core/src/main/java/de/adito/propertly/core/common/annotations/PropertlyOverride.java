package de.adito.propertly.core.common.annotations;

import java.lang.annotation.*;

/**
 * This annotation indicates similarly to {@link java.lang.Override} that the annotated description overrides another
 * description. But PropertlyOverride is evaluated at runtime.
 *
 * @author j.boesl, 22.06.15
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertlyOverride
{
}
