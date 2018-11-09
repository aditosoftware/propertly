package de.adito.propertly.reactive.impl;

import de.adito.propertly.core.spi.*;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Factory to create the "real" observables - do not use this outside the impl-package!
 *
 * @author w.glanzer, 01.11.2018
 * @see de.adito.propertly.reactive.api.PropertlyObservables
 */
public abstract class InternalObservableFactory
{

  private InternalObservableFactory()
  {
  }

  public static <P extends IPropertyPitProvider, V> Observable<Optional<IProperty<P, V>>> property(IProperty<P, V> pProperty)
  {
    return property(pProperty, true);
  }

  public static <P extends IPropertyPitProvider, V> Observable<Optional<IProperty<P, V>>> property(IProperty<P, V> pProperty, boolean pCompleteWhenInvalid)
  {
    return Observable.create(new PropertyObservable<>(pProperty, pCompleteWhenInvalid))
        .startWith(pProperty)
        .map(Optional::of);
  }

  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T> Observable<Optional<S>> propertyPit(@NotNull S pPropertyPit)
  {
    return propertyPit(pPropertyPit, true);
  }

  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T> Observable<Optional<S>> propertyPit(@NotNull S pPropertyPit, boolean pCompleteWhenInvalid)
  {
    return Observable.create(new PropertyPitObservable<>(pPropertyPit, pCompleteWhenInvalid))
        .startWith(pPropertyPit)
        .map(Optional::of);
  }

}
