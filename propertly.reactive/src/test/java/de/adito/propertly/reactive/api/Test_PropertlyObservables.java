package de.adito.propertly.reactive.api;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.reactive.impl.dummies.ReactivePropertyPitProvider;
import de.adito.propertly.reactive.impl.util.BehaviorConsumer;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test for the general "PropertlyObservables"-API-Class.
 * It contains common use cases for the reactive package
 *
 * @author w.glanzer, 06.11.2018
 */
@SuppressWarnings({"FieldCanBeLocal", "unchecked"})
public class Test_PropertlyObservables
{

  private ReactivePropertyPitProvider rootPPP;
  private ReactivePropertyPitProvider.Child1 childInContainer1;
  private BehaviorConsumer<Optional<Integer>> spiedConsumer;

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
  void test_commonAPIUseCase()
  {
    // Set Initial Value to 1337
    childInContainer1.setValue(ReactivePropertyPitProvider.Child1.type, 1337);

    // Build Chain
    Disposable disposable = PropertlyObservables.propertyPit(rootPPP)
        .switchMap(pRootPPP -> PropertlyObservables.optPropertyValuePit(pRootPPP, ReactivePropertyPitProvider.children))
        .switchMap(pChildren -> PropertlyObservables.optPropertyValuePit(pChildren, ReactivePropertyPitProvider.ChildContainer1.child))
        .switchMap(pChild -> PropertlyObservables.optPropertyValue(pChild, ReactivePropertyPitProvider.Child1.type))
        .subscribe(spiedConsumer);

    // Initial Value, nothing else -> One Call
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(Optional.of(1337));
    clearInvocations(spiedConsumer);

    IProperty<ReactivePropertyPitProvider, ReactivePropertyPitProvider.ChildContainer1> childrenProp = ((ReactivePropertyPitProvider.ChildContainer1) childInContainer1.getParent()).getOwnProperty();
    childrenProp.setValue(null);

    // Chain is now "broken" and should fire that Optional is empty
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(Optional.empty());
    clearInvocations(spiedConsumer);

    // Rebuild Chain
    ReactivePropertyPitProvider.ChildContainer1 container1 = childrenProp.setValue(new ReactivePropertyPitProvider.ChildContainer1());
    childInContainer1 = container1.setValue(ReactivePropertyPitProvider.ChildContainer1.child, new ReactivePropertyPitProvider.Child1());

    // Two calls, because we changed the chain twice
    verify(spiedConsumer, times(2)).accept(any());
    verify(spiedConsumer, times(2)).accept(Optional.empty());
    clearInvocations(spiedConsumer);

    // Change Property Value, one call to our consumer
    childInContainer1.setValue(ReactivePropertyPitProvider.Child1.type, 42);
    verify(spiedConsumer).accept(any());
    verify(spiedConsumer).accept(Optional.of(42));
    clearInvocations(spiedConsumer);

    // Completely dispose chain if disposable was called
    disposable.dispose();

    // No interaction with our consumer!
    childInContainer1.setValue(ReactivePropertyPitProvider.Child1.type, -1337);
    verify(spiedConsumer, times(0)).accept(any());
  }

}
