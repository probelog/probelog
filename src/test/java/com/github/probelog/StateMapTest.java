package com.github.probelog;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

import static com.github.probelog.State.*;
import static com.github.probelog.StateMap.validTransitions;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class StateMapTest {

    @Test
    public void unknown() {
        assertEquals(createStateSet(CREATED, INITIALIZED), validTransitions(UNKNOWN));
    }

    @Test
    public void created() {
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED), validTransitions(CREATED));
    }

    @Test
    public void initialized() {
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED), validTransitions(INITIALIZED));
    }

    @Test
    public void updated() {
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED), validTransitions(UPDATED));
    }

    @Test
    public void pasted() {
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED), validTransitions(PASTED));
    }

    @Test
    public void touched() {
        assertEquals(createStateSet(UPDATED, TOUCHED), validTransitions(TOUCHED));
    }

    Set<State> createStateSet(State... states) {
        return new HashSet<State>(asList(states));
    }

}
