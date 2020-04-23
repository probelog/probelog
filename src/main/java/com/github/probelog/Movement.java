package com.github.probelog;

import java.util.Objects;

public class Movement {

    private final String file, fromState, toState;

    public Movement(String file, String fromState, String toState) {
        this.file = file;
        this.fromState=fromState;
        this.toState=toState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return Objects.equals(file, movement.file) &&
                Objects.equals(fromState, movement.fromState) &&
                Objects.equals(toState, movement.toState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, fromState, toState);
    }

}
