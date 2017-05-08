package com.vgorcinschi.rimmanew.util;

import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.credential.CredentialQueryCandidate;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javaslang.control.Try;
import javaslang.control.Validation;

/**
 * utility methods based on the open-source
 * JavaSlang library
 * @author vgorcinschi
 */
public class JavaSlangUtil {
    
    public static <T> Validation<Exception, T[]> arrayNonEmpty(T[] array){
        /**
         * the test is that the passed array contains at least one non null
         * value.
         */
        return (array != null && array.length > 0 
                && Arrays.stream(array)
                        .anyMatch(elem -> elem !=null && !"".equals(elem.toString()))) ?
                Validation.valid(array)
                :Validation.invalid(new IllegalArgumentException("Array "+Arrays.deepToString(array)+" is effectively empty."));
    }
    
    public static <T> Try<T> fromComplFuture(CompletableFuture<T> future){
        try {
            T answer = future.get(500, TimeUnit.MILLISECONDS);
            return Try.success(answer);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            return Try.failure(ex);
        }
    }
}