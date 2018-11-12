package de.adito.propertly.reactive.impl.builder;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.reactive.api.builder.IBuilderPropertyObservable;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author w.glanzer, 09.11.2018
 */
public class BuilderPropertyObservable<S extends IPropertyPitProvider, V> extends AbstractBuilderObservable<IProperty<S, V>>
    implements IBuilderPropertyObservable<S, V>
{

  private final boolean completeWhenInvalid;

  public BuilderPropertyObservable(@NotNull Observable<Optional<IProperty<S, V>>> pProperty, boolean pCompleteWhenInvalid)
  {
    super(pProperty);
    completeWhenInvalid = pCompleteWhenInvalid;
  }

  @NotNull
  @Override
  public Observable<Optional<V>> emitValue()
  {
    return getInternalObservable()
        .map(pOpt -> pOpt
            .filter(IProperty::canRead)
            .map(IProperty::getValue));
  }

}
