package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.spi.*;
import io.reactivex.rxjava3.core.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A PropertyListObservable is a wrapper around an Observable with
 * a list of properties in it.
 *
 * @param <S> PropertyPitProvider all properties belong to
 * @param <T> Value-Type of all properties
 * @author w.glanzer, 09.11.2018
 */
public interface IBuilderPropertyListObservable<S extends IPropertyPitProvider, T> extends IBuilderObservable<List<IProperty<S, T>>>
{

  /**
   * Constructs an Observable that contains the values of all
   * properties in this PropertyListObservable.  <br>
   * The resulting Observable is not distincted automatically.<br>
   * All properties are observed, so the Observable will get notified if any of the
   * containing properties changes.
   * <b>It does not contain any NULL-Values</b>. If you have the need to, use {@link IBuilderPropertyListObservable#emitValues(boolean)}
   *
   * @return the Observable, not <tt>null</tt>
   * @see IBuilderPropertyListObservable#emitDistinctedValues()
   */
  @NotNull
  default Observable<Optional<List<T>>> emitValues()
  {
    return emitValues(false);
  }

  /**
   * Constructs an Observable that contains the values of all
   * properties in this PropertyListObservable.  <br>
   * The resulting Observable is not distincted automatically. <br>
   * All properties are observed, so the Observable will get notified if any of the
   * containing properties changes.
   *
   * @param pIncludeNullValues <tt>true</tt>, if the inner list should contain NULL-Values, if a property has <tt>null</tt> as a value
   * @return the Observable, not <tt>null</tt>
   * @see IBuilderPropertyListObservable#emitDistinctedValues(boolean)
   */
  @NotNull
  Observable<Optional<List<T>>> emitValues(boolean pIncludeNullValues);

  /**
   * Constructs an Observable that contains the values of all
   * properties in this PropertyListObservable. <br>
   * The resulting Observable <b>will be distincted automatically</b><br>
   * All properties are observed, so the Observable will get notified if any of the
   * containing properties changes. <br>
   * <b>It does not contain any NULL-Values</b>. If you have the need to, use {@link IBuilderPropertyListObservable#emitDistinctedValues(boolean)}
   *
   * @return the Observable, not <tt>null</tt>
   */
  @NotNull
  default Observable<Optional<List<T>>> emitDistinctedValues()
  {
    return emitValues()
        .distinctUntilChanged();
  }

  /**
   * Constructs an Observable that contains the values of all
   * properties in this PropertyListObservable. <br>
   * The resulting Observable <b>will be distincted automatically</b><br>
   * All properties are observed, so the Observable will get notified if any of the
   * containing properties changes.
   *
   * @param pIncludeNullValues <tt>true</tt>, if the inner list should contain NULL-Values, if a property has <tt>null</tt> as a value
   * @return the Observable, not <tt>null</tt>
   */
  @NotNull
  default Observable<Optional<List<T>>> emitDistinctedValues(boolean pIncludeNullValues)
  {
    return emitValues(pIncludeNullValues)
        .distinctUntilChanged();
  }

}
