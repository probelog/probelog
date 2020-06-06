package com.github.probelog;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MakingChanges {

    @Test
    public void sequentialChange() {

        ChangeMaker maker = new ChangeMaker();
        maker.consumeCreate("x");
        Change first = maker.makeChange();
        List<FileState> afters = first.afters();
        assertEquals(1, afters.size());
        assertEquals("name:x,state:newly created", afters.get(0).toString());
        assertNull(afters.get(0).before());
        Change second = maker.makeChange();
        assertEquals(first, second.previousChange());

    }

}
