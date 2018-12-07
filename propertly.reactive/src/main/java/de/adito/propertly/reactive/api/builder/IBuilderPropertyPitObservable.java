package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.spi.*;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

/**
 * a BuilderPropertyPitObservable is a Observable that
 * contains a PropertyPitProvider as value and will trigger,
 * if propertyAdded/propertyOrderChanged/propertyRemoved is fired
 *
 * @param <P> Parent of the containing PropertyPitProvider
 * @param <S> Self-Type of the containing PropertyPitProvider
 * @param <T> Value-Type the containing PropertyPitProvider can contain
 * @author w.glanzer, 09.11.2018
 */
public interface IBuilderPropertyPitObservable<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T>
    extends IBuilderObservable<S>
{

  /**
   * Emits a single Property, identified by the given PropertyDescription. <br>
   * If the property is not found in this pit, the resulting BuilderPropertyObservable will be empty. <br>
   * The PropertyObservable will trigger, if the property will be added/changed/removed
   *
   * @param pDescription Description to identify the property to observe on
   * @param <T2>         Value-Type of the property and it's description
   * @return the IBuilderPropertyObservable, not <tt>null</tt>
   */
  @NotNull <T2> IBuilderPropertyObservable<S, T2> emitProperty(@NotNull IPropertyDescription<? super S, T2> pDescription);

  /**
   * Emits a child PropertyPitProvider, identified by the given PropertyDescription. <br>
   * If the property is not found in this pit, the resulting BuilderPropertyPitObservable will be empty <br>
   * The PropertyPitObservable will trigger, if the property that contains the searched pit will be added/changed/removed or
   * the pit itself does something like propertyAdded/propertyOrderChanged/propertyRemoved.
   *
   * @param pDescription Description to identify the property (with an PropertyPitProvider as value-type) to observe on
   * @param <S2>         Value-Type of the property -> It has to be a subtype of PropertyPitProvider!
   * @return IBuilderPropertyPitObservable, not <tt>null</tt>
   */
  @NotNull <P2 extends IPropertyPitProvider, S2 extends IPropertyPitProvider<P2, S2, T2>, T2>
  IBuilderPropertyPitObservable<P2, S2, T2> emitPropertyPit(@NotNull IPropertyDescription<? super S, S2> pDescription);

  /**
   * Emits all properties this PropertyPitProvider has.
   *
   * @return IBuilderPropertyListObservable, not <tt>null</tt>
   */
  @NotNull
  IBuilderPropertyListObservable<S, T> emitProperties();

  /**
   * Returns a new PropertyObservable of our hierarchy property
   *
   * @return the PropertyObservable
   */
  @NotNull
  IBuilderPropertyObservable<IPropertyPitProvider, ?> emitHierarchyValue();

  /**
   * Emits all values of all properties this PropertyPit has.
   * It calls {@link IBuilderPropertyPitObservable#emitProperties()} first, to retrieve a list of all properties,
   * and then emits their values with {@link IBuilderPropertyListObservable#emitValues()}
   * The resulting Observable is not distincted automatically.
   * <b>It does not contain any NULL-Values</b>. If you have the need to, use {@link IBuilderPropertyPitObservable#emitPropertyValues(boolean)}
   *
   * @return the Observable containing all values as list, not <tt>null</tt>
   */
  @NotNull
  default Observable<Optional<List<T>>> emitPropertyValues()
  {
    return emitProperties().emitValues();
  }

  /**
   * Emits all values of all properties this PropertyPit has.
   * It calls {@link IBuilderPropertyPitObservable#emitProperties()} first, to retrieve a list of all properties,
   * and then emits their values with {@link IBuilderPropertyListObservable#emitValues()}
   * The resulting Observable is not distincted automatically.
   *
   * @param pIncludeNullValues <tt>true</tt>, if the inner list should contain NULL-Values, if a property has <tt>null</tt> as a value
   * @return the Observable containing all values as list, not <tt>null</tt>
   */
  @NotNull
  default Observable<Optional<List<T>>> emitPropertyValues(boolean pIncludeNullValues)
  {
    return emitProperties().emitValues(pIncludeNullValues);
  }

}
