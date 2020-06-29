package com.github.probelog.file;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class ActionMap {

    private static Set<Action> existingAndValid = createActionSet(Action.CREATED, Action.INITIALIZED, Action.UPDATED, Action.PASTED, Action.COPIED);
    private static Set<Action> deleted = createActionSet(Action.DELETED, Action.CUT);

    static Set<Action> validFollowOnActions(Action previousAction) {
        if (previousAction== Action.UNKNOWN)
            return createActionSet(Action.CREATED, Action.INITIALIZED, Action.NOT_EXISTING);
        if (previousAction== Action.NOT_EXISTING)
           return createActionSet(Action.PASTED);
        if (existingAndValid.contains(previousAction))
            return createActionSet(Action.DELETED, Action.CUT, Action.COPIED, Action.PASTED, Action.UPDATED);
        if (deleted.contains(previousAction))
            return createActionSet(Action.CREATED, Action.PASTED, Action.NOT_EXISTING);
        throw new RuntimeException("BUG !! Broken State Map !!");
    }

    static Set<Action> createActionSet(Action... states) {
        return new HashSet<>(asList(states));
    }

}
