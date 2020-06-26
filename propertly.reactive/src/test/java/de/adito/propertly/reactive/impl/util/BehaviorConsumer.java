package de.adito.propertly.reactive.impl.util;

import io.reactivex.rxjava3.functions.Consumer;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author w.glanzer, 30.10.2018
 */
public class BehaviorConsumer<T> implements Consumer<T>
{

  private AtomicReference<T> lastValue = new AtomicReference<>(null);

  @Override
  public void accept(T pT)
  {
    lastValue.set(pT);
  }

  public void setCompleted()
  {
  }

  public T getValue()
  {
    return lastValue.get();
  }
}
