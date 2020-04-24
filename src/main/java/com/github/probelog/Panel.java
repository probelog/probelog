package com.github.probelog;

import java.util.Set;

public class Panel {

    private final Panel previous;
    private final Set<Movement> movements;

    public Panel(Panel previous, Set<Movement> movements) {
        this.previous=previous;
        this.movements=movements;
    }

    public Set<Movement> getMovements() {
        return movements;
    }

    public Panel getPrevious() {
        return previous;
    }

    public String getState(String file) {
        for(Movement movement: movements) {
            if (movement.getFile().equals(file))
                return
        }
    }
}
