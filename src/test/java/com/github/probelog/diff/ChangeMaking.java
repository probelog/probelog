package com.github.probelog.diff;

import org.junit.Assert;
import org.junit.Test;

import static com.github.probelog.diff.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static org.junit.Assert.assertEquals;

public class ChangeMaking {

    private final String before = ChangeMaker.createStringWithLineSeparatorDelimiters("line1","  line2 ","line3");

    @Test
    public void changeString() {

        ChangeMaker changeMaker = new ChangeMaker(before);
        changeMaker.replace("  line2 ", " line2Changed").
                insert(" line2Changed", "line2.1", "line2.2").
                remove("line1");
        Assert.assertEquals(ChangeMaker.createStringWithLineSeparatorDelimiters(" line2Changed","line2.1", "line2.2","line3"),changeMaker.changed());

    }

}
