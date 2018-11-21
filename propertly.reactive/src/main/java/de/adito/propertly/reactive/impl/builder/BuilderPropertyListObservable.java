package de.adito.propertly.reactive.impl.builder;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.reactive.api.builder.IBuilderPropertyListObservable;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author w.glanzer, 09.11.2018
 */
public class BuilderPropertyListObservable<S extends IPropertyPitProvider, T>
    extends AbstractBuilderObservable<List<IProperty<S, T>>>
    implements IBuilderPropertyListObservable<S, T>
{

  public BuilderPropertyListObservable(@NotNull Observable<Optional<List<IProperty<S, T>>>> pObservable)
  {
    super(pObservable);
  }

  @NotNull
  @Override
  public Observable<Optional<List<T>>> emitValues(boolean pIncludeNullValues)
  {
    return getInternalObservable()
        .map(pPropertyListOpt -> {
          if (!pPropertyListOpt.isPresent())
            return Optional.empty();
          List<T> valueList = pPropertyListOpt.get().stream()
              .map(pProp -> pProp.canRead() ? pProp.getValue() : null)
              .filter(pValue -> pValue != null || pIncludeNullValues)
              .collect(Collectors.toList());
          return Optional.of(valueList);
        });
  }

}
