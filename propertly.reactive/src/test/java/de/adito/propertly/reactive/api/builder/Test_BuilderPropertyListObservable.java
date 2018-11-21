package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.reactive.impl.dummies.ReactivePropertyPitProvider;
import de.adito.propertly.reactive.impl.util.BehaviorConsumer;
import org.junit.jupiter.api.*;

import java.util.*;

import static de.adito.propertly.reactive.OptionalMatcher.optionalWithValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author w.glanzer, 08.11.2018
 */
@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
public class Test_BuilderPropertyListObservable
{
  private ReactivePropertyPitProvider rootPPP;
  private ReactivePropertyPitProvider.Properties properties;
  private BehaviorConsumer spiedConsumer;

  @SuppressWarnings("ConstantConditions")
  @BeforeEach
  void setUp()
  {
    rootPPP = new Hierarchy<>(UUID.randomUUID().toString(), new ReactivePropertyPitProvider()).getValue();
    properties = rootPPP.setValue(ReactivePropertyPitProvider.stringProperties, new ReactivePropertyPitProvider.Properties());
    spiedConsumer = spy(new BehaviorConsumer<>());
  }

  @Test
  void test_emitValues()
  {
    PropertlyObservableBuilder.create(rootPPP)
        .emitPropertyPit(ReactivePropertyPitProvider.stringProperties)
        .emitProperties()
        .emitValues()
        .doOnNext(System.out::println)
        .subscribe(spiedConsumer);

    // Initial Value: Empty List, because the "stringProperties"-Property was already set
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(optionalWithValue(Collections.emptyList()));
    clearInvocations(spiedConsumer);

    // Add something to our list
    assert properties != null;
    IProperty<ReactivePropertyPitProvider.Properties, String> newProperty = properties.addProperty("new property");

    // Now the consumer should be called once, because of propertyAdded and propertyValueChanged
    verify(spiedConsumer, times(2)).accept(any());

    // Our list must contain one property, with the value "new property"
    Optional<List<String>> listOptional = (Optional<List<String>>) spiedConsumer.getValue();
    assertTrue(listOptional.isPresent());
    List<String> propertyList = listOptional.get();
    assertEquals(1, propertyList.size());
    assertEquals("new property", propertyList.get(0));
    clearInvocations(spiedConsumer);

    // Change the value of the previous added property
    newProperty.setValue("new value");
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(optionalWithValue(Collections.singletonList("new value")));
    clearInvocations(spiedConsumer);

    // Remove previous added property
    properties.removeProperty(newProperty);
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(optionalWithValue(Collections.emptyList()));
  }

}
