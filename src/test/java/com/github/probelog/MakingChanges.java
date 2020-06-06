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
        maker.consumeCreate("y");
        Change first = maker.makeChange();
        List<FileState> afters = first.afters();
        assertEquals(2, afters.size());
        FileState xCreate = afters.get(0);
        FileState yCreate = afters.get(1);
        assertEquals("name:x,state:newly created", xCreate.toString());
        assertNull(xCreate.before());
        assertEquals("name:y,state:newly created", yCreate.toString());
        assertNull(yCreate.before());
        Change second = maker.makeChange();
        assertEquals(first, second.previousChange());

    }

}
