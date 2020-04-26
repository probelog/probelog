package com.github.probelog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileEventTest {

    @Test
    public void previousEventForFile() {

        FileEvent file1event1 = new FileEvent("file1");
        FileEvent file2event1 = new FileEvent("file2");
        FileEvent file1event2 = new FileEvent("file1");

        file2event1.setPrevious(file1event1);
        file1event2.setPrevious(file2event1);
        assertEquals(file1event1, file1event2.getOlderSibling());

    }


}
