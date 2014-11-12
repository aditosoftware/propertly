package de.verpalnt.propertly.core.api;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL, 09.11.13
 */
public interface IPropertyPit<S extends IPropertyPitProvider, T> extends IPropertyPitProvider<S, T>, Iterable<IProperty<S, ? extends T>>
{

  @Nonnull
  S getSource();

  @Nullable
  IPropertyPitProvider<?, ?> getParent();

  @Nonnull
  IProperty<?, S> getOwnProperty();

  @Nullable
  <E> IProperty<S, E> findProperty(IPropertyDescription<?, E> pPropertyDescription);

  @Nonnull
  <E extends T> IProperty<S, E> getProperty(IPropertyDescription<? super S, E> pPropertyDescription);

  @Nullable
  <E extends T> E getValue(IPropertyDescription<? super S, E> pPropertyDescription);

  @Nullable
  <E extends T> E setValue(IPropertyDescription<? super S, E> pPropertyDescription, E pValue);

  @Nonnull
  Set<IPropertyDescription<S, ? extends T>> getPropertyDescriptions();

  @Nonnull
  List<IProperty<S, ? extends T>> getProperties();

  void addPropertyEventListener(IPropertyEventListener pListener);

  void removePropertyEventListener(IPropertyEventListener pListener);

}
