/// Copyright (c) 2014 Daniel S. Dickstein
//from https://github.com/ddickstein/Java-Library/blob/master/java8/function/TriConsumer.java
package com.vgorcinschi.rimmanew.helpers;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
  public void accept(T t, U u, V v);

  public default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
    Objects.requireNonNull(after);
    return (a, b, c) -> {
      accept(a, b, c);
      after.accept(a, b, c);
    };
  }
}