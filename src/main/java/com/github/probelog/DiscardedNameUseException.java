package com.github.probelog;

public class DiscardedNameUseException extends IllegalStateException {

    DiscardedNameUseException(String message) {
        super(message);
    }

    static DiscardedNameUseException illegalSource(String fromName, String toName) {
        throw new DiscardedNameUseException("Trying to use discarded name: " + fromName + " as source for: " +toName);
    }

    static DiscardedNameUseException illegalUpdate(String name) {
        throw new DiscardedNameUseException("Trying to update using a discarded name: " + name);
    }

}
