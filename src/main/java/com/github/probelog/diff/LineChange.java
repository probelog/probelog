package com.github.probelog.diff;

import static com.github.probelog.diff.LineChange.Type.*;

class LineChange {

    enum Type {
        INSERT;

    }
    private Type type;

    LineChange(Type type) {
        this.type = type;
    }

    Type type() {
        return INSERT;
    }

}
