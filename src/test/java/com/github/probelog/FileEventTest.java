package com.github.probelog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileEventTest {

    @Test
    public void previousEventForFile() {

        FileEvent file1event1 = new FileEvent("file1");
        FileEvent file1event2 = new FileEvent("file1");

        file1event2.setPrevious(file1event1);
        assertEquals(file1event1, file1event2.getOlderSibling());

    }


}
