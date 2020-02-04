package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.spi.*;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A BuilderPropertyObservable is a wrapper around an Observable that
 * contains a single Property as value. <br>
 * It will trigger, if propertyNameChanged/propertyValueChanged is fired
 *
 * @param <S> PropertyPitProvider this property belongs to
 * @param <T> Value-Type of the property
 * @author w.glanzer, 09.11.2018
 */
public interface IBuilderPropertyObservable<S extends IPropertyPitProvider, T> extends IBuilderObservable<IProperty<S, T>>
{

  /**
   * Casts the value of the current property to a PropertyPit and creates a new PropertyPitObservable.
   * If the value is not an instance of IPropertyPitProvider, no specific exception will be thrown -&gt; Runtime ClassCastException!
   *
   * @return the PropertyPitObservable
   */
  @NotNull
  <P2 extends IPropertyPitProvider, S2 extends IPropertyPitProvider<P2, S2, T2>, T2> IBuilderPropertyPitObservable<P2, S2, T2> asPropertyPit();

  /**
   * Returns a new PropertyObservable of our hierarchy property
   *
   * @return the PropertyObservable
   */
  @NotNull
  IBuilderPropertyObservable<IPropertyPitProvider, ?> emitHierarchyValue();

  /**
   * Constructs an Observable that contains an Optional representing
   * the value of the underlying property. <br>
   * The Optional will be empty, if the property has NULL as value. <br>
   * This Observable is not distincted automatically.
   *
   * @return the Observable, not <tt>null</tt>
   */
  @NotNull
  Observable<Optional<T>> emitValue();

  /**
   * Constructs an Observable that contains an Optional representing
   * the value of the underlying property. <br>
   * The Optional will be empty, if the property has NULL as value. <br>
   * This Observable is will be distincted automatically.
   *
   * @return the Observable, not <tt>null</tt>
   */
  @NotNull
  default Observable<Optional<T>> emitDistinctedValue()
  {
    return emitValue()
        .distinctUntilChanged();
  }

}
