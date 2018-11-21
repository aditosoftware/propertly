package de.adito.propertly.reactive.impl;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.reactive.impl.dummies.ReactivePropertyPitProvider;
import de.adito.propertly.reactive.impl.util.BehaviorConsumer;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for "PropertyObservable"
 *
 * @author w.glanzer, 30.10.2018
 * @see PropertyObservable
 */
@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
public class Test_PropertyObservable
{
  private ReactivePropertyPitProvider rootPPP;
  private IProperty<?, String> stringProperty;
  private BehaviorConsumer<IProperty<?, String>> subscribedConsumer;
  private Observable stringPropertyObservable;
  private Disposable disposable;

  @BeforeEach
  void setUp()
  {
    rootPPP = new Hierarchy<>(UUID.randomUUID().toString(), new ReactivePropertyPitProvider()).getValue();
    stringProperty = rootPPP.getProperty(ReactivePropertyPitProvider.stringProperty);
    stringPropertyObservable = InternalObservableFactory.property(stringProperty, true).map(Optional::get);
    subscribedConsumer = spy(new BehaviorConsumer<>());
    disposable = stringPropertyObservable.subscribe(subscribedConsumer);
    clearInvocations(subscribedConsumer);
  }

  @Test
  void test_initialValue()
  {
    assertEquals(stringProperty, subscribedConsumer.getValue());
  }

  @Test
  void test_fixed_valueChange()
  {
    stringProperty.setValue("newValue");
    verify(subscribedConsumer, times(1)).accept(any());
    assertEquals("newValue", subscribedConsumer.getValue().getValue());
  }

  @Test
  void test_fixed_dipose()
  {
    disposable.dispose();
    stringProperty.setValue("this_value_must_not_be_fired");
    verify(subscribedConsumer, times(0)).accept(any());
  }

  @Test
  void test_dynamic_rename()
  {
    // Init property
    ReactivePropertyPitProvider.Properties props = rootPPP.setValue(ReactivePropertyPitProvider.stringProperties, new ReactivePropertyPitProvider.Properties());
    assert props != null;
    stringProperty = props.addProperty("myProperty1", "noValue");
    disposable = InternalObservableFactory.property(stringProperty, true).map(Optional::get).subscribe(subscribedConsumer, e -> {}, subscribedConsumer::setCompleted);
    clearInvocations(subscribedConsumer);

    // Rename Property
    stringProperty.rename("myNewName");

    // Disposable should now be disposed, and listener removed
    verify(subscribedConsumer, times(1)).accept(any());
  }

  @Test
  void test_dynamic_remove()
  {
    // Init property
    ReactivePropertyPitProvider.Properties props = rootPPP.setValue(ReactivePropertyPitProvider.stringProperties, new ReactivePropertyPitProvider.Properties());
    assert props != null;
    stringProperty = props.addProperty("myProperty1", "noValue");
    disposable = InternalObservableFactory.property(stringProperty, true).map(Optional::get).subscribe(subscribedConsumer, e -> {}, subscribedConsumer::setCompleted);
    clearInvocations(subscribedConsumer);

    // Remove property -> Make it invalid
    props.removeProperty((IProperty) stringProperty);

    // Disposable should now be disposed, and listener removed
    assertTrue(disposable.isDisposed());
    verify(subscribedConsumer, times(1)).setCompleted();
    verify(subscribedConsumer, times(0)).accept(any());
  }

  @Test
  void test_subscribeAfterOneDisposed()
  {
    // Dispose the previously subscribed consumer
    disposable.dispose();

    // Re-Subscribe it
    disposable = stringPropertyObservable.subscribe(subscribedConsumer);
    clearInvocations(subscribedConsumer);

    // Set New Value -> Fire one time
    stringProperty.setValue("testValue");
    verify(subscribedConsumer).accept(any());
    assertEquals("testValue", subscribedConsumer.getValue().getValue());
  }
}
