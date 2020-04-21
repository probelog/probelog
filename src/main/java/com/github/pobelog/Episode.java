package com.github.pobelog;

import java.util.Set;

public class Episode {

    private Set<Movement> movements;

    Episode(Set<Movement> movements) {
        this.movements = movements;
    }

    public Set<Movement> movements() {
        return movements;
    }
}
