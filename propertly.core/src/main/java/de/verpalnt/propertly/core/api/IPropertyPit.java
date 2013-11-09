package de.verpalnt.propertly.core.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Created by PaL on 09.11.13.
 */
public interface IPropertyPit<S extends IPropertyPitProvider> extends IPropertyPitProvider<S>, Iterable<IProperty<S, ?>>
{

  @Nonnull
  S getSource();

  @Nullable
  IPropertyPitProvider getParent();

  @Nullable
  <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> findProperty(
      IPropertyDescription<SOURCE, T> pPropertyDescription);

  @Nonnull
  <SOURCE extends IPropertyPitProvider, T> IProperty<SOURCE, T> getProperty(
      IPropertyDescription<SOURCE, T> pPropertyDescription);

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
