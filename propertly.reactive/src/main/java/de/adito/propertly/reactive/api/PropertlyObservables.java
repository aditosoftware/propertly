package de.adito.propertly.reactive.api;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.reactive.impl.InternalObservableFactory;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author W.Glanzer, 30.04.2018
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class PropertlyObservables
{

  private PropertlyObservables()
  {
  }

  @NotNull
  public static <P extends IPropertyPitProvider<?, P, ?>, V> Observable<IProperty<P, V>> property(@NotNull IProperty<P, V> pProperty)
  {
    return property(pProperty, true);
  }

  @NotNull
  public static <P extends IPropertyPitProvider<?, P, ?>, V> Observable<IProperty<P, V>> property(@NotNull IProperty<P, V> pProperty, boolean pCompleteWhenInvalid)
  {
    return InternalObservableFactory.property(pProperty, pCompleteWhenInvalid);
  }

  @NotNull
  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T> Observable<S> propertyPit(@NotNull S pPropertyPit)
  {
    return InternalObservableFactory.propertyPit(pPropertyPit);
  }

  @NotNull
  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T extends IPropertyPitProvider> Observable<List<T>> propertyPitValues(@NotNull S pPropertyPit)
  {
    return propertyPit(pPropertyPit)
        .switchMap(pPit -> {
          List<Observable<T>> children = pPit.getPit().getValues().stream()
              .map(pT -> (Observable<T>) propertyPit(pT))
              .collect(Collectors.toList());
          return Observable.combineLatest(children, pTArray -> {
            List<T> result = new ArrayList<>();
            for (Object o : pTArray)
              result.add((T) o);
            return result;
          });
        });
  }

  // OPTIONALS

  @NotNull
  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, V>, V, VP>
  Observable<Optional<IProperty<S, VP>>> optProperty(@NotNull Optional<S> pProvider, @NotNull IPropertyDescription<? super S, VP> pDescription)
  {
    if (!pProvider.isPresent())
      return Observable.just(Optional.empty());
    IProperty<S, VP> prop = pProvider.get().getPit().findProperty(pDescription);
    if (prop == null)
      return Observable.just(Optional.empty());
    return property(prop).map(Optional::of);
  }

  @NotNull
  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, V>, V, VP>
  Observable<Optional<VP>> optPropertyValue(@NotNull Optional<S> pProvider, @NotNull IPropertyDescription<? super S, VP> pDescription)
  {
    if (!pProvider.isPresent())
      return Observable.just(Optional.empty());
    IProperty<S, VP> prop = pProvider.get().getPit().findProperty(pDescription);
    if (prop == null)
      return Observable.just(Optional.empty());
    return property(prop).map(pProp -> Optional.ofNullable(pProp.getValue()));
  }

  @NotNull
  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, V>, V, VP>
  Observable<Optional<VP>> optPropertyValue(@NotNull S pProvider, @NotNull IPropertyDescription<? super S, VP> pDescription)
  {
    return optPropertyValue(Optional.of(pProvider), pDescription);
  }

  @NotNull
  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, V>, V,
      VP extends IPropertyPitProvider>
  Observable<Optional<VP>> optPropertyValuePit(@NotNull Optional<S> pProvider, @NotNull IPropertyDescription<? super S, VP> pDescription)
  {
    return optProperty(pProvider, pDescription)
        .switchMap(pProp -> {
          if(!pProp.isPresent())
            return Observable.just(Optional.empty());
          VP value = pProp.get().getValue();
          if (value == null)
            return Observable.just(Optional.empty());
          return propertyPit(value)
              .map(Optional::of);
        });
  }

  @NotNull
  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, V>, V,
      VP extends IPropertyPitProvider>
  Observable<Optional<VP>> optPropertyValuePit(@NotNull S pProvider, @NotNull IPropertyDescription<? super S, VP> pDescription)
  {
    return optPropertyValuePit(Optional.of(pProvider), pDescription);
  }

}
