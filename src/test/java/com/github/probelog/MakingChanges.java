package com.github.probelog;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MakingChanges {

    @Test
    public void sequentialChange() {

        ChangeMaker maker = new ChangeMaker();
        maker.consumeCreate("y");
        maker.consumeCreate("x");
        Change first = maker.makeChange();
        List<FileChange> fileChanges = first.fileChanges();
        assertEquals(2, fileChanges.size());
        FileChange xCreate = fileChanges.get(0);
        FileChange yCreate = fileChanges.get(1);
        assertEquals("name:x,state:newly created", xCreate.afterState().toString());
        assertNull(xCreate.beforeState());
        assertEquals("name:y,state:newly created", yCreate.afterState().toString());
        assertNull(yCreate.beforeState());
        Change second = maker.makeChange();
        assertEquals(first, second.previousChange());

    }

}
