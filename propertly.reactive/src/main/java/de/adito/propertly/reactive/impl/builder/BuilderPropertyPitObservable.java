package de.adito.propertly.reactive.impl.builder;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.reactive.api.builder.*;
import de.adito.propertly.reactive.impl.InternalObservableFactory;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 09.11.2018
 */
public class BuilderPropertyPitObservable<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    extends AbstractBuilderObservable<S>
    implements IBuilderPropertyPitObservable<P, S, T>
{

  private final boolean completeWhenInvalid;

  public BuilderPropertyPitObservable(@NotNull Observable<Optional<S>> pPropertyPitProvider, boolean pCompleteWhenInvalid)
  {
    super(pPropertyPitProvider);
    completeWhenInvalid = pCompleteWhenInvalid;
  }

  @NotNull
  @Override
  public <T2> BuilderPropertyObservable<S, T2> emitProperty(@NotNull IPropertyDescription<? super S, T2> pDescription)
  {
    Observable<Optional<IProperty<S, T2>>> propertyObservable = getInternalObservable()
        .switchMap(pS -> {
          if (pS.isPresent())
          {
            IProperty<S, T2> property = pS.get().getPit().findProperty(pDescription);
            if (property != null)
              return InternalObservableFactory.property(property, completeWhenInvalid);
          }

          return Observable.just(Optional.empty());
        });
    return new BuilderPropertyObservable<>(propertyObservable, completeWhenInvalid);
  }

  @NotNull
  @Override
  public <P2 extends IPropertyPitProvider, S2 extends IPropertyPitProvider<P2, S2, T2>, T2> BuilderPropertyPitObservable<P2, S2, T2> emitPropertyPit(@NotNull IPropertyDescription<? super S, S2> pDescription)
  {
    Observable<Optional<S2>> propertyPitObservable = emitProperty(pDescription)
        .emitValue()
        .switchMap(pPitOpt -> {
          if (!pPitOpt.isPresent())
            return Observable.just(Optional.empty());
          S2 ppp = pPitOpt.get();
          return InternalObservableFactory.propertyPit(ppp, completeWhenInvalid);
        });
    return new BuilderPropertyPitObservable<>(propertyPitObservable, completeWhenInvalid);
  }

  @NotNull
  @Override
  public BuilderPropertyListObservable<S, T> emitProperties()
  {
    Observable<Optional<List<IProperty<S, T>>>> propertyList = getInternalObservable()
        .switchMap(pS -> {
          if(!pS.isPresent())
            return Observable.just(Optional.empty());
          List<Observable<Optional<IProperty<S, T>>>> properties = pS.get().getPit().getProperties().stream()
              .map(pProp -> emitProperty(pProp.getDescription()).getInternalObservable())
              .collect(Collectors.toList());
          if(properties.isEmpty())
            return Observable.just(Optional.of(Collections.emptyList()));
          return Observable.combineLatest(properties, pPropOptionals -> {
            List<IProperty<S, T>> props = new ArrayList<>();
            for (Object opt : pPropOptionals)
              //noinspection unchecked -- We have to cast, cause rxJava does not give us an appropriate type
              ((Optional<IProperty<S, T>>) opt).ifPresent(props::add);
            return Optional.of(props);
          });
        });
    return new BuilderPropertyListObservable<>(propertyList);
  }

  @NotNull
  @Override
  public IBuilderPropertyObservable<IPropertyPitProvider, ?> emitHierarchyValue()
  {
    Observable hierarchyPropObs = getInternalObservable()
        .map(pOpt -> pOpt.map(pProp -> pProp.getPit().getHierarchy().getProperty()))
        .distinctUntilChanged();
    return new BuilderPropertyObservable<>(hierarchyPropObs, completeWhenInvalid);
  }
}
