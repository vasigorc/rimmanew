
package com.vgorcinschi.rimmanew.helpers;


@FunctionalInterface
public interface TriConsumer<T, U, V> {
  public void accept(T t, U u, V v);
}