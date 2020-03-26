package com.github.pobelog;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class TestBedResetter {

    public static final String TESTBED_LOCATION = "/src/test/testbed";

    public static File resetTestBed() {
        File testbed = retrieveDirectory();
        clearDirectory(testbed);
        return testbed;
    }

    private static File retrieveDirectory() {
        File testbed = new File(new File("").getAbsolutePath() + TESTBED_LOCATION);
        if (!testbed.isDirectory()) {
            testbed.delete();
            testbed.mkdir();
        }
        return testbed;
    }

    private static void clearDirectory(File testbed) {
        for (File child : testbed.listFiles())
            delete(child);
    }

    private static void delete(File file) {
        if (!file.isDirectory())  {
            file.delete();
            return;
        }
        clearDirectory(file);
        file.delete();
    }

}
