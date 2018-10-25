package de.adito.propertly.core.spi.extension;

import de.adito.propertly.core.spi.IMutablePropertyPitProvider;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

/**
 * @author j.boesl, 30.10.14
 */
abstract class AbstractMutablePropertyPitProviderBase
    <P extends IPropertyPitProvider, S extends IMutablePropertyPitProvider<P, S, T>, T>
    extends AbstractPropertyPitProviderBase<P, S, T>
    implements IMutablePropertyPitProvider<P, S, T>
{

  @NotNull
  public <E extends T> IProperty<S, E> addProperty(@NotNull E pValue)
  {
    return getPit().addProperty(pValue);
  }

  @NotNull
  public <E extends T> IProperty<S, E> addProperty(@NotNull String pName, @NotNull E pValue)
  {
    return getPit().addProperty(pName, pValue);
  }

  @NotNull
  public <E extends T> IProperty<S, E> addProperty(@NotNull IPropertyDescription<S, E> pPropertyDescription)
  {
    return getPit().addProperty(pPropertyDescription);
  }

  @NotNull
  public <E extends T> IProperty<S, E> addProperty(@NotNull Class<E> pType, @NotNull String pName,
                                                   @Nullable Annotation... pAnnotations)
  {
    return getPit().addProperty(pType, pName, pAnnotations);
  }

  public boolean removeProperty(@NotNull IPropertyDescription<? super S, T> pPropertyDescription)
  {
    return getPit().removeProperty(pPropertyDescription);
  }

  public boolean removeProperty(@NotNull IProperty<S, ? extends T> pProperty)
  {
    return getPit().removeProperty(pProperty);
  }
}
