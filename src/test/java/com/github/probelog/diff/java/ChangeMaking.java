package com.github.probelog.diff.java;

import org.junit.Test;

import static com.github.probelog.diff.java.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static org.junit.Assert.assertEquals;

public class ChangeMaking {

    private final String before = createStringWithLineSeparatorDelimiters("line1","  line2 ","line3");

    @Test
    public void changeString() {

        ChangeMaker changeMaker = new ChangeMaker(before);
        changeMaker.replace("  line2 ", " line2Changed").
                insert(" line2Changed", "line2.1", "line2.2").
                remove("line1");
        assertEquals(createStringWithLineSeparatorDelimiters(" line2Changed","line2.1", "line2.2","line3"),changeMaker.changed());

    }

}
