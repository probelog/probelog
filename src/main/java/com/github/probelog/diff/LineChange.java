package com.github.probelog.diff;

import static com.github.probelog.diff.LineChange.Type.*;

class LineChange {

    enum Type {
        INSERT,
        DELETE,
        NOCHANGE
    }

    private Type type;
    private String line;

    LineChange(Type type, String line) {
        this.type = type;
        this.line=line;
    }

    Type type() {
        return type;
    }

    public String line() {
        return line;
    }

}
