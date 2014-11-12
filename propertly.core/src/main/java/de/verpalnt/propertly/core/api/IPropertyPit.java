package de.verpalnt.propertly.core.api;

import javax.annotation.*;
import java.util.*;

/**
 * @author PaL, 09.11.13
 */
public interface IPropertyPit<S extends IPropertyPitProvider> extends IPropertyPitProvider<S>, Iterable<IProperty<S, ?>>
{

  @Nonnull
  S getSource();

  @Nullable
  IPropertyPitProvider<?> getParent();

  @Nonnull IProperty<?, S> getOwnProperty();

  @Nullable
  <T> IProperty<S, T> findProperty(IPropertyDescription<?, T> pPropertyDescription);

  @Nonnull
  <T> IProperty<S, T> getProperty(IPropertyDescription<? super S, T> pPropertyDescription);

  @Nullable
  <T> T getValue(IPropertyDescription<? super S, T> pPropertyDescription);

  @Nullable
  <T> T setValue(IPropertyDescription<? super S, T> pPropertyDescription, T pValue);

  Set<IPropertyDescription> getPropertyDescriptions();

  @Nonnull
  List<IProperty<S, ?>> getProperties();

  void addPropertyEventListener(IPropertyEventListener pListener);

  void removePropertyEventListener(IPropertyEventListener pListener);

}
