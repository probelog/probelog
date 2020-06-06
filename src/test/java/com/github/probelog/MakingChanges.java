package com.github.probelog;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MakingChanges {

    @Test
    public void sequentialChange() {

        ChangeMaker maker = new ChangeMaker();
        maker.consumeCreate("x");
        Change first = maker.makeChange();
        List<FileState> after = first.after();
        assertEquals(1, after.size());
        Change second = maker.makeChange();
        assertEquals(first, second.previousChange());

    }

}
