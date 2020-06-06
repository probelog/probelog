package com.github.probelog;

public class Change {

    private Change previous;

    Change(Change previous) {
        this.previous=previous;
    }

    public Change previousChange() {
        return previous;
    }
}
