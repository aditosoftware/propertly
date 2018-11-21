package de.adito.propertly.reactive.impl.dummies;

import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.spi.*;
import de.adito.propertly.core.spi.extension.*;

/**
 * @author w.glanzer, 30.10.2018
 */
public class ReactivePropertyPitProvider extends AbstractPPP<IPropertyPitProvider, ReactivePropertyPitProvider, Object>
{

  public static final IPropertyDescription<ReactivePropertyPitProvider, String> stringProperty = PD.create(ReactivePropertyPitProvider.class);

  public static final IPropertyDescription<ReactivePropertyPitProvider, Properties> stringProperties = PD.create(ReactivePropertyPitProvider.class);

  public static final IPropertyDescription<ReactivePropertyPitProvider, ChildContainer1> children = PD.create(ReactivePropertyPitProvider.class);

  public static class Properties extends AbstractIndexedMutablePPP<IPropertyPitProvider, Properties, String>
  {
    public Properties()
    {
      super(String.class);
    }
  }

  public static class ChildContainer1 extends AbstractPPP<ReactivePropertyPitProvider, ChildContainer1, Object>
  {
    public static final IPropertyDescription<ChildContainer1, ChildContainer2> children = PD.create(ChildContainer1.class);
    public static final IPropertyDescription<ChildContainer1, Child1> child = PD.create(ChildContainer1.class);
  }

  public static class ChildContainer2 extends AbstractIndexedMutablePPP<ChildContainer1, ChildContainer2, Child1>
  {
    public ChildContainer2()
    {
      super(Child1.class);
    }
  }

  public static class Child1 extends AbstractPPP<IPropertyPitProvider, Child1, Object>
  {
    public static final IPropertyDescription<Child1, Integer> type = PD.create(Child1.class);
  }

}
