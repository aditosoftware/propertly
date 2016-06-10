package de.adito.propertly.core.spi;

import javax.annotation.Nonnull;

/**
 * IHierarchy is the source of IPropertyPitProvider structures. Each IPropertyPitProvider is assigned to an IHierarchy.
 * Further each change in the structure can be registered with listeners.
 *
 * @author j.boesl, 13.01.15
 */
public interface IHierarchy<T extends IPropertyPitProvider>
{

  /**
   * @return this Hierarchy's root IProperty.
   */
  IProperty<IPropertyPitProvider, T> getProperty();

  /**
   * @return this Hierarchy's root IPropertyPitProvider.
   */
  T getValue();

  /**
   * Adds a weak listener.
   *
   * @param pListener the listener to be weakly added.
   */
  void addWeakListener(@Nonnull IPropertyPitEventListener<?, ?> pListener);

  /**
   * Adds a strong listener.
   *
   * @param pListener the listener to be strongly added.
   */
  void addStrongListener(@Nonnull IPropertyPitEventListener<?, ?> pListener);

  /**
   * Removes a listener.
   *
   * @param pListener the listener to be removed.
   */
  void removeListener(@Nonnull IPropertyPitEventListener<?, ?> pListener);
}
