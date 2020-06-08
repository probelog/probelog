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
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, UPDATED, TOUCHED), validTransitions(CREATED));
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, UPDATED, TOUCHED), validTransitions(INITIALIZED));
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, UPDATED, TOUCHED), validTransitions(UPDATED));
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, UPDATED, TOUCHED), validTransitions(PASTED));
        assertEquals(createStateSet(DELETED, CUT, COPIED, PASTED, UPDATED, TOUCHED), validTransitions(COPIED));
        assertEquals(createStateSet(UPDATED, TOUCHED), validTransitions(TOUCHED));
        assertEquals(createStateSet(CREATED, PASTED), validTransitions(DELETED));
        assertEquals(createStateSet(CREATED, PASTED), validTransitions(CUT));
    }

    Set<State> createStateSet(State... states) {
        return new HashSet<State>(asList(states));
    }

}
