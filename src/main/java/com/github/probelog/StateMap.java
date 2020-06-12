package com.github.probelog;

import java.util.HashSet;
import java.util.Set;

import static com.github.probelog.State.*;
import static java.util.Arrays.asList;

public class StateMap {

    private static Set<State> existingAndValid = createStateSet(CREATED, INITIALIZED, UPDATED, PASTED, COPIED);
    private static Set<State> deleted = createStateSet(DELETED, CUT);

    static Set<State> validTransitions(State fromState) {
        if (fromState==UNKNOWN)
            return createStateSet(CREATED, INITIALIZED, NOT_EXISTING);
        if (fromState==NOT_EXISTING)
           return createStateSet(PASTED);
        if (existingAndValid.contains(fromState))
            return createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED, UPDATED);
        if (fromState==TOUCHED)
            return createStateSet(UPDATED, TOUCHED);
        if (deleted.contains(fromState))
            return createStateSet(CREATED, PASTED);
        throw new RuntimeException("BUG !! Broken State Map !!");
    }

    static Set<State> createStateSet(State... states) {
        return new HashSet<>(asList(states));
    }

}
