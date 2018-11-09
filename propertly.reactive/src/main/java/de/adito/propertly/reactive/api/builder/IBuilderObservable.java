package de.adito.propertly.reactive.api.builder;

import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author w.glanzer, 09.11.2018
 */
public interface IBuilderObservable<V>
{

  @NotNull
  Observable<Optional<V>> toObservable();

  @NotNull
  Observable<V> toObservableUnsafe();

}
