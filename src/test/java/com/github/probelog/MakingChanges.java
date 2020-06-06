package com.github.probelog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MakingChanges {

    @Test
    public void initialisation() {

        ChangeMaker maker = new ChangeMaker();
        Change first = maker.makeChange();
        Change second = maker.makeChange();
        assertEquals(first, second.previousChange());

    }

}
