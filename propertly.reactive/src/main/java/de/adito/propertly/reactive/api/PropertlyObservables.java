package de.adito.propertly.reactive.api;

import de.adito.propertly.core.spi.*;
import de.adito.propertly.reactive.impl.InternalObservableFactory;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author w.glanzer, 09.11.2018
 */
public class PropertlyObservables
{

  private PropertlyObservables()
  {
  }

  public static <P extends IPropertyPitProvider, V> Observable<Optional<IProperty<P, V>>> property(IProperty<P, V> pProperty)
  {
    return InternalObservableFactory.property(pProperty);
  }

  public static <P extends IPropertyPitProvider, V> Observable<Optional<IProperty<P, V>>> property(IProperty<P, V> pProperty, boolean pCompleteWhenInvalid)
  {
    return InternalObservableFactory.property(pProperty, pCompleteWhenInvalid);
  }

  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T> Observable<Optional<S>> propertyPit(@NotNull S pPropertyPit)
  {
    return InternalObservableFactory.propertyPit(pPropertyPit);
  }

  public static <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, T>, T> Observable<Optional<S>> propertyPit(@NotNull S pPropertyPit, boolean pCompleteWhenInvalid)
  {
    return InternalObservableFactory.propertyPit(pPropertyPit, pCompleteWhenInvalid);
  }

}
