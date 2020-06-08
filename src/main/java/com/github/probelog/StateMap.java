package com.github.probelog;

import java.util.HashSet;
import java.util.Set;

import static com.github.probelog.State.*;
import static java.util.Arrays.asList;

public class StateMap {

    static Set<State> validTransitions(State fromState) {
        if (fromState==UNKNOWN)
            return createStateSet(CREATED, INITIALIZED);
        return createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED);
    }

    static Set<State> createStateSet(State... states) {
        return new HashSet<State>(asList(states));
    }

}
