package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.spi.*;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author w.glanzer, 09.11.2018
 */
public interface IBuilderPropertyObservable<S extends IPropertyPitProvider, T> extends IBuilderObservable<IProperty<S, T>>
{

  @NotNull
  Observable<Optional<T>> emitValue();

}
