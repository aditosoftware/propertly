package de.adito.propertly.reactive.impl;

import de.adito.propertly.core.spi.*;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

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

  public static <P extends IPropertyPitProvider<?, P, ?>, V> Observable<IProperty<P, V>> property(IProperty<P, V> pProperty, boolean pCompleteWhenInvalid)
  {
    return Observable.create(new PropertyObservable<>(pProperty, pCompleteWhenInvalid))
        .startWith(pProperty);
  }

  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T> Observable<S> propertyPit(@NotNull S pPropertyPit)
  {
    return Observable.create(new PropertyPitObservable<>(pPropertyPit))
        .startWith(pPropertyPit);
  }

}
