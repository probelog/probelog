package com.github.probelog;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FileStateTest {

    @Test
    public void findingLatestVersions() {

        FileState file1state1 = new FileState("file1");
        FileState file2state1 = new FileState("file2");
        FileState file1state2 = new FileState("file1");
        FileState file2state2 = new FileState("file2");

        file2state1.setPrevious(file1state1);
        file1state2.setPrevious(file2state1);
        file2state2.setPrevious(file1state2);
        assertEquals(new HashSet<FileState>(Arrays.asList(file2state2, file1state2)),file2state2.findYoungestVersions());

    }


}
