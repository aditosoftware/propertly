package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.reactive.impl.InternalObservableFactory;
import de.adito.propertly.reactive.impl.builder.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 08.11.2018
 */
public class PropertlyObservableBuilder
{

  private PropertlyObservableBuilder()
  {
  }

  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T> IBuilderPropertyPitObservable<P, S, T> create(@NotNull IPropertyPitProvider<P, S, T> pPropertyPitProvider)
  {
    return new BuilderPropertyPitObservable<>(InternalObservableFactory.propertyPit((S) pPropertyPitProvider, false), false);
  }

  public static <P extends IPropertyPitProvider, V> IBuilderPropertyObservable<P, V> create(@NotNull IProperty<P, V> pProperty)
  {
    return new BuilderPropertyObservable<>(InternalObservableFactory.property(pProperty, false), false);
  }

}
