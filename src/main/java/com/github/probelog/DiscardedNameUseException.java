package com.github.probelog;

public class DiscardedNameUseException extends IllegalStateException {

    public static final String NO_LONGER_EXISTS = "no longer exists";

    DiscardedNameUseException(String message) {
        super(message);
    }

    static DiscardedNameUseException illegalSource(String fromName, String toName) {
        return new DiscardedNameUseException("Trying to use discarded name: " + fromName + " as source for: " +toName);
    }

    static DiscardedNameUseException illegalDelete(String name) {
        return new DiscardedNameUseException("Trying to delete a discarded name: " + name);
    }



}
