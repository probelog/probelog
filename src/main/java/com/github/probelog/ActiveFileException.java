package com.github.probelog;

public class ActiveFileException extends IllegalStateException {

    ActiveFileException(String message) {
        super(message);
    }

    static ActiveFileException illegalCreate(String name) {
        throw new ActiveFileException("Trying to create using an active name: " + name);
    }

    static ActiveFileException illegalMoveCreate(String fromFile, String toFile) {
        throw new ActiveFileException("Trying to rename: " + fromFile +" to an active name: " + toFile);
    }

}
