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
   * Constructs an Observable that contains an Optional representing
   * the value of the underlying property. <br>
   * The Optional will be empty, if the property has NULL as value. <br>
   * This Observable is not distincted automatically.
   *
   * @return the Observable, not <tt>null</tt>
   */
  @NotNull
  Observable<Optional<T>> emitValue();

}
