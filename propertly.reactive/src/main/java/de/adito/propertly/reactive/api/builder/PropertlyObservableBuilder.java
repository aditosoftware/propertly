package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.reactive.impl.InternalObservableFactory;
import de.adito.propertly.reactive.impl.builder.*;
import org.jetbrains.annotations.NotNull;

/**
 * Builder for all {@link IBuilderObservable}s, the main entrypoint for
 * interacting with the API of "propertly.reactive"
 *
 * @author w.glanzer, 08.11.2018
 */
public class PropertlyObservableBuilder
{

  private PropertlyObservableBuilder()
  {
  }

  /**
   * Creates an PropertyPitObservable from a PropertyPitProvider.
   *
   * @param pPropertyPitProvider Source, to start building the reactive chain
   * @return IBuilderPropertyPitProvider, not <tt>null</tt>
   */
  @NotNull
  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
  IBuilderPropertyPitObservable<P, S, T> create(@NotNull IPropertyPitProvider<P, S, T> pPropertyPitProvider)
  {
    return new BuilderPropertyPitObservable<>(InternalObservableFactory.propertyPit((S) pPropertyPitProvider, false), false);
  }

  /**
   * Creates an PropertyObservable from a Property.
   *
   * @param pProperty Source, to start building the reactive chain
   * @return IBuilderPropertyObservable, not <tt>null</tt>
   */
  @NotNull
  public static <P extends IPropertyPitProvider, V> IBuilderPropertyObservable<P, V> create(@NotNull IProperty<P, V> pProperty)
  {
    return new BuilderPropertyObservable<>(InternalObservableFactory.property(pProperty, false), false);
  }

}
