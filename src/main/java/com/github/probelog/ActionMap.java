package com.github.probelog;

import java.util.HashSet;
import java.util.Set;

import static com.github.probelog.Action.*;
import static java.util.Arrays.asList;

public class ActionMap {

    private static Set<Action> existingAndValid = createActionSet(CREATED, INITIALIZED, UPDATED, PASTED, COPIED);
    private static Set<Action> deleted = createActionSet(DELETED, CUT);

    static Set<Action> validFollowOnActions(Action previousAction) {
        if (previousAction==UNKNOWN)
            return createActionSet(CREATED, INITIALIZED, NOT_EXISTING);
        if (previousAction==NOT_EXISTING)
           return createActionSet(PASTED);
        if (existingAndValid.contains(previousAction))
            return createActionSet(DELETED, CUT, COPIED, PASTED, UPDATED);
        if (deleted.contains(previousAction))
            return createActionSet(CREATED, PASTED, NOT_EXISTING);
        throw new RuntimeException("BUG !! Broken State Map !!");
    }

    static Set<Action> createActionSet(Action... states) {
        return new HashSet<>(asList(states));
    }

}
