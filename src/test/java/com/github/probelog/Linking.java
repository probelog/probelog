package com.github.probelog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Linking {

    @Test
    public void basicLinking() {

        Linker linker = new Linker();
        FileEvent firstUpdateToFileA = linker.addFileUpdate("fileA");
        FileEvent firstUpdateToFileB = linker.addFileUpdate("fileB");

        assertNull(firstUpdateToFileA.previousEvent());
        assertEquals(firstUpdateToFileA,firstUpdateToFileB.previousEvent());

    }

}
