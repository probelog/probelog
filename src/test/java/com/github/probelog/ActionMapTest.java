package com.github.probelog;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

import static com.github.probelog.Action.*;
import static com.github.probelog.ActionMap.validFollowOnActions;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ActionMapTest {

    @Test
    public void testValidFollowOnActions() {
        assertEquals(createActionSet(CREATED, INITIALIZED, NOT_EXISTING), validFollowOnActions(UNKNOWN));
        assertEquals(createActionSet(PASTED), validFollowOnActions(NOT_EXISTING));
        assertEquals(createActionSet(DELETED, CUT, COPIED, PASTED, UPDATED), validFollowOnActions(CREATED));
        assertEquals(createActionSet(DELETED, CUT, COPIED, PASTED, UPDATED), validFollowOnActions(INITIALIZED));
        assertEquals(createActionSet(DELETED, CUT, COPIED, PASTED, UPDATED), validFollowOnActions(UPDATED));
        assertEquals(createActionSet(DELETED, CUT, COPIED, PASTED, UPDATED), validFollowOnActions(PASTED));
        assertEquals(createActionSet(DELETED, CUT, COPIED, PASTED, UPDATED), validFollowOnActions(COPIED));
        assertEquals(createActionSet(CREATED, PASTED, NOT_EXISTING), validFollowOnActions(DELETED));
        assertEquals(createActionSet(CREATED, PASTED, NOT_EXISTING), validFollowOnActions(CUT));
    }

    Set<Action> createActionSet(Action... states) {
        return new HashSet<>(asList(states));
    }

}
