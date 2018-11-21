package de.adito.propertly.reactive.api.builder;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.reactive.impl.dummies.ReactivePropertyPitProvider;
import de.adito.propertly.reactive.impl.util.BehaviorConsumer;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.mockito.Mockito.*;

/**
 * @author w.glanzer, 09.11.2018
 */
@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
public class Test_BuilderPropertyObservable
{
  private ReactivePropertyPitProvider rootPPP;
  private BehaviorConsumer spiedConsumer;

  @BeforeEach
  void setUp()
  {
    rootPPP = new Hierarchy<>(UUID.randomUUID().toString(), new ReactivePropertyPitProvider()).getValue();
    spiedConsumer = spy(new BehaviorConsumer<>());
  }

  @Test
  void test_emitValue()
  {
    PropertlyObservableBuilder.create(rootPPP)
        .emitProperty(ReactivePropertyPitProvider.stringProperty)
        .emitValue()
        .subscribe(spiedConsumer);

    // Initial Value
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(Optional.empty());
    clearInvocations(spiedConsumer);

    // Fire new Value
    rootPPP.setValue(ReactivePropertyPitProvider.stringProperty, "test");
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(Optional.of("test"));
  }

}
