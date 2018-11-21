package de.adito.propertly.reactive.impl.builder;

import de.adito.propertly.reactive.api.builder.IBuilderObservable;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author w.glanzer, 09.11.2018
 */
abstract class AbstractBuilderObservable<V> implements IBuilderObservable<V>
{

  private final Observable<Optional<V>> observable;

  public AbstractBuilderObservable(@NotNull Observable<Optional<V>> pObservable)
  {
    observable = pObservable;
  }

  @NotNull
  @Override
  public Observable<Optional<V>> toObservable()
  {
    return observable;
  }

  @NotNull
  @Override
  public Observable<V> toObservableUnsafe()
  {
    return toObservable()
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  @NotNull
  protected Observable<Optional<V>> getInternalObservable()
  {
    return observable;
  }

}
