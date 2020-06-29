package com.github.probelog.file;

import java.util.HashSet;
import java.util.Set;

import static com.github.probelog.file.Action.*;
import static java.util.Arrays.asList;

public class ActionMap {

    private static Set<Action> existingAndValid = createActionSet(CREATED, INITIALIZED, UPDATED, PASTED, COPIED);
    private static Set<Action> deleted = createActionSet(DELETED, CUT);

    static Set<Action> validFollowOnActions(Action action) {
        if (action== UNKNOWN)
            return createActionSet(CREATED, INITIALIZED, NOT_EXISTING);
        if (action== NOT_EXISTING)
           return createActionSet(PASTED);
        if (existingAndValid.contains(action))
            return createActionSet(DELETED, CUT, COPIED, PASTED, UPDATED);
        if (deleted.contains(action))
            return createActionSet(CREATED, PASTED, NOT_EXISTING);
        throw new RuntimeException("BUG !! Broken State Map !!");
    }

    static Set<Action> createActionSet(Action... states) {
        return new HashSet<>(asList(states));
    }

}
