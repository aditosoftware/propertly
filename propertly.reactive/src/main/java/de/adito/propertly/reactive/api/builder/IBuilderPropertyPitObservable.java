package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.spi.*;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author w.glanzer, 09.11.2018
 */
public interface IBuilderPropertyPitObservable<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    extends IBuilderObservable<S>
{

  @NotNull
  <T2> IBuilderPropertyObservable<S, T2> emitProperty(@NotNull IPropertyDescription<? super S, T2> pDescription);

  @NotNull
  <P2 extends IPropertyPitProvider, S2 extends IPropertyPitProvider<P2, S2, T2>, T2> IBuilderPropertyPitObservable<P2, S2, T2> emitPropertyPit(@NotNull IPropertyDescription<? super S, S2> pDescription);

  @NotNull
  IBuilderPropertyListObservable<S, T> emitProperties();

  @NotNull
  default Observable<Optional<List<T>>> emitPropertyValues()
  {
    return emitProperties().emitValues();
  }

  @NotNull
  default Observable<Optional<List<T>>> emitPropertyValues(boolean pIncludeNullValues)
  {
    return emitProperties().emitValues(pIncludeNullValues);
  }

}
