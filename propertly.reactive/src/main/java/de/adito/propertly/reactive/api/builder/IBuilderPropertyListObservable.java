package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.spi.*;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author w.glanzer, 09.11.2018
 */
public interface IBuilderPropertyListObservable<S extends IPropertyPitProvider, T> extends IBuilderObservable<List<IProperty<S, T>>>
{

  @NotNull
  Observable<Optional<List<T>>> emitValues(boolean pIncludeNullValues);

  @NotNull
  default Observable<Optional<List<T>>> emitValues()
  {
    return emitValues(false);
  }

  @NotNull
  default Observable<Optional<List<T>>> emitDistinctedValues()
  {
    return emitValues()
        .distinctUntilChanged();
  }

  @NotNull
  default Observable<Optional<List<T>>> emitDistinctedValues(boolean pIncludeNullValues)
  {
    return emitValues(pIncludeNullValues)
        .distinctUntilChanged();
  }

}
