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
    public void testValidTransitions() {
        assertEquals(createStateSet(CREATED, INITIALIZED), validTransitions(UNKNOWN));
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED), validTransitions(CREATED));
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED), validTransitions(INITIALIZED));
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED), validTransitions(UPDATED));
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, TOUCHED), validTransitions(PASTED));
        assertEquals(createStateSet(UPDATED, TOUCHED), validTransitions(TOUCHED));
    }

    Set<State> createStateSet(State... states) {
        return new HashSet<State>(asList(states));
    }

}
