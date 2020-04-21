package com.github.pobelog;

import java.util.Set;

public class Recorder {
    public Episode record(Set<Movement> movements) {
        return new Episode(movements);
    }
}
