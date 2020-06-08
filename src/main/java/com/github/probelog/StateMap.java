package com.github.probelog;

import java.util.HashSet;
import java.util.Set;

import static com.github.probelog.State.CREATED;
import static com.github.probelog.State.INITIALIZED;
import static java.util.Arrays.asList;

public class StateMap {

    static Set<State> validTransitions(State fromState) {
        return createStateSet(CREATED, INITIALIZED);
    }

    static Set<State> createStateSet(State... states) {
        return new HashSet<State>(asList(states));
    }

}
