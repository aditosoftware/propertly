package de.adito.propertly.reactive.api.builder;

import io.reactivex.rxjava3.core.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Common Interface for all Propertly-Observables constructed by PropertlyObservableBuilder
 *
 * @see PropertlyObservableBuilder
 * @author w.glanzer, 09.11.2018
 */
public interface IBuilderObservable<V>
{

  /**
   * Constructs the underlying observable.
   * The Value is an Optional of the "real" value, because RxJava
   * does not allow to return NULL inside one Observable.
   * The returned Observable is not distincted automatically.
   *
   * @return the observable, not <tt>null</tt>
   */
  @NotNull
  Observable<Optional<V>> toObservable();

  /**
   * Constructs the underlying observable.
   * The Value is the outboxed value of {@link IBuilderObservable#toObservable()},
   * previously filtered by {@link Optional#isPresent()}.
   * Use this with care - you will not be notified with NULL-Value-Changes (e.g. propertyValueChange with newValue=NULL).
   * The returned Observable is not distincted automatically.
   *
   * @return the observable, not <tt>null</tt>
   */
  @NotNull
  Observable<V> toObservableUnsafe();

}
