package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.reactive.impl.dummies.ReactivePropertyPitProvider;
import de.adito.propertly.reactive.impl.util.BehaviorConsumer;
import io.reactivex.rxjava3.functions.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static de.adito.propertly.reactive.OptionalMatcher.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author w.glanzer, 08.11.2018
 */
@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
public class Test_BuilderPropertyPitObservable
{
  private ReactivePropertyPitProvider rootPPP;
  private ReactivePropertyPitProvider.Child1 childInContainer1;
  private BehaviorConsumer spiedConsumer;

  @SuppressWarnings("ConstantConditions")
  @BeforeEach
  void setUp()
  {
    rootPPP = new Hierarchy<>(UUID.randomUUID().toString(), new ReactivePropertyPitProvider()).getValue();
    ReactivePropertyPitProvider.ChildContainer1 childContainer1 = rootPPP.setValue(ReactivePropertyPitProvider.children, new ReactivePropertyPitProvider.ChildContainer1());
    childInContainer1 = childContainer1.setValue(ReactivePropertyPitProvider.ChildContainer1.child, new ReactivePropertyPitProvider.Child1());
    spiedConsumer = spy(new BehaviorConsumer<>());
  }

  @Test
  void test_emitPropertyPit()
  {
    PropertlyObservableBuilder.create(rootPPP)
        .emitPropertyPit(ReactivePropertyPitProvider.children)
        .toObservable()
        .subscribe(spiedConsumer);

    // Initial Value
    verify(spiedConsumer).accept(optionalWithValue(ReactivePropertyPitProvider.ChildContainer1.class));
    clearInvocations(spiedConsumer);

    // Invalidate children-Property
    rootPPP.setValue(ReactivePropertyPitProvider.children, null);
    verify(spiedConsumer).accept(optionalEmpty());
  }

  @Test
  void test_emitPropertiesFromInvalidPropertyPit() throws Throwable
  {
    // init mocks
    Consumer onNextConsumer = mock(Consumer.class);
    Consumer onErrorConsumer = mock(Consumer.class);
    Action onComplete = mock(Action.class);

    // init mutable PropertyPitProvider
    ReactivePropertyPitProvider.ChildContainer1 staticContainer = rootPPP.getValue(ReactivePropertyPitProvider.children);
    assertNotNull(staticContainer);

    // invalidate container, so we got an invalid provider
    rootPPP.setValue(ReactivePropertyPitProvider.children, new ReactivePropertyPitProvider.ChildContainer1());

    // init observable with invalid container
    PropertlyObservableBuilder.create(staticContainer)
        .emitPropertyPit(ReactivePropertyPitProvider.ChildContainer1.children)
        .emitPropertyValues()
        .subscribe(onNextConsumer, onErrorConsumer, onComplete);

    // verify that nothing happened
    verify(onNextConsumer).accept(optionalEmpty());
    verifyNoMoreInteractions(onNextConsumer);
    verifyZeroInteractions(onErrorConsumer);
    verify(onComplete).run();
    verifyNoMoreInteractions(onComplete);
  }

  @Test
  void test_emitDynamicProperties() throws Throwable
  {
    // init mocks
    Consumer onNextConsumer = mock(Consumer.class);
    Consumer onErrorConsumer = mock(Consumer.class);
    Action onComplete = mock(Action.class);

    // init mutable PropertyPitProvider
    ReactivePropertyPitProvider.ChildContainer1 staticContainer = rootPPP.getValue(ReactivePropertyPitProvider.children);
    assertNotNull(staticContainer);
    ReactivePropertyPitProvider.ChildContainer2 mutableContainer = staticContainer.setValue(ReactivePropertyPitProvider.ChildContainer1.children,
                                                                                            new ReactivePropertyPitProvider.ChildContainer2());
    assertNotNull(mutableContainer);

    // init observable
    PropertlyObservableBuilder.create(staticContainer)
        .emitPropertyPit(ReactivePropertyPitProvider.ChildContainer1.children)
        .emitPropertyValues()
        .subscribe(onNextConsumer, onErrorConsumer, onComplete);

    // check, if we got the initial value
    verify(onNextConsumer).accept(optionalWithValue(new ArrayList<>()));
    verifyNoMoreInteractions(onNextConsumer);
    verifyZeroInteractions(onErrorConsumer);
    verifyZeroInteractions(onComplete);
    clearInvocations(onNextConsumer, onErrorConsumer, onComplete);

    // add some children
    IProperty<ReactivePropertyPitProvider.ChildContainer2, ReactivePropertyPitProvider.Child1> prop = mutableContainer.addProperty(new ReactivePropertyPitProvider.Child1());
    IProperty<ReactivePropertyPitProvider.ChildContainer2, ReactivePropertyPitProvider.Child1> prop2 = mutableContainer.addProperty(new ReactivePropertyPitProvider.Child1());

    // check, if we got the new children
    verify(onNextConsumer).accept(optionalWithValue(new ArrayList<>()));
    verify(onNextConsumer, times(2)).accept(optionalWithValue(Collections.singletonList(prop.getValue())));
    verify(onNextConsumer).accept(optionalWithValue(Arrays.asList(prop.getValue(), prop2.getValue())));
    verifyNoMoreInteractions(onNextConsumer);
    verifyZeroInteractions(onErrorConsumer);
    verifyZeroInteractions(onComplete);
    clearInvocations(onNextConsumer, onErrorConsumer, onComplete);

    // remove a child
    mutableContainer.removeProperty(prop);

    // check, if we received, that a child was removed
    verify(onNextConsumer).accept(optionalWithValue(Collections.singletonList(prop2.getValue())));
    verifyNoMoreInteractions(onNextConsumer);
    verifyZeroInteractions(onErrorConsumer);
    verifyZeroInteractions(onComplete);
    clearInvocations(onNextConsumer, onErrorConsumer, onComplete);

    // invalidate the whole property pit provider by setting a new children object
    rootPPP.setValue(ReactivePropertyPitProvider.children, new ReactivePropertyPitProvider.ChildContainer1());

    // check, if we receive nothing anymore
    verifyZeroInteractions(onNextConsumer);
    verifyZeroInteractions(onErrorConsumer);
    verifyZeroInteractions(onComplete);
  }

  @Test
  void test_emitProperties()
  {
    PropertlyObservableBuilder.create(rootPPP)
        .emitPropertyPit(ReactivePropertyPitProvider.stringProperties)
        .emitProperties()
        .toObservable()
        .doOnNext(System.out::println)
        .subscribe(spiedConsumer);

    // Initial Value: Empty Optional, because the stringProperties-Property is null
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(optionalEmpty());
    clearInvocations(spiedConsumer);

    // Set Value of "stringProperties"-Value, so that the List should be empty now
    ReactivePropertyPitProvider.Properties properties = rootPPP.setValue(ReactivePropertyPitProvider.stringProperties, new ReactivePropertyPitProvider.Properties());
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(optionalWithValue(Collections.emptyList()));
    clearInvocations(spiedConsumer);

    // Add something to our list
    assert properties != null;
    IProperty<ReactivePropertyPitProvider.Properties, String> newProperty = properties.addProperty("new property");

    // Now the consumer should be called twice, because of propertyAdded and propertyValueChanged
    verify(spiedConsumer, times(2)).accept(any());

    // Our list must contain one property, with the value "new property"
    Optional<List<IProperty<ReactivePropertyPitProvider.Properties, String>>> listOptional = (Optional<List<IProperty<ReactivePropertyPitProvider.Properties, String>>>) spiedConsumer.getValue();
    assertTrue(listOptional.isPresent());
    List<IProperty<ReactivePropertyPitProvider.Properties, String>> propertyList = listOptional.get();
    assertEquals(1, propertyList.size());
    assertEquals("new property", propertyList.get(0).getValue());
    clearInvocations(spiedConsumer);

    // Change the value of the previous added property
    newProperty.setValue("new value");
    verify(spiedConsumer).accept(any());
    listOptional = (Optional<List<IProperty<ReactivePropertyPitProvider.Properties, String>>>) spiedConsumer.getValue();
    assertTrue(listOptional.isPresent());
    assertEquals("new value", listOptional.get().get(0).getValue());
    clearInvocations(spiedConsumer);

    // Remove previous added property
    properties.removeProperty(newProperty);
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(optionalWithValue(Collections.emptyList()));
  }

}
