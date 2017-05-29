package com.vgorcinschi.rimmanew.util;

import java.util.concurrent.CompletableFuture;
import rx.Observable;

/**
 *
 * @author vgorcinschi
 */
public class RxJavaUtil {

    public static <T> Observable<T> observe(CompletableFuture<T> future) {
        return Observable.create(subscriber -> {
            future.whenComplete((value, exception) -> {
                if (exception != null) {
                    subscriber.onError(exception);
                } else {
                    subscriber.onNext(value);
                    subscriber.onCompleted();
                }
            });
        });
    }
}
