package com.github.probelog;

import java.util.HashMap;
import java.util.Map;

public class Linker {

    public static final String ALREADY_EXISTS = "already exists";
    private int sequence=1;
    private Map<String, FileEvent> fileEventsMap = new HashMap<>();

    public FileEvent addFileUpdate(String file) {

        FileEvent result = new FileEvent(file, sequence++, getPreviousEventForFile(file));
        fileEventsMap.put(file, result);
        return result;

    }

    public FileEvent addFileRename(String fromFile, String toFile) {

        if (fileEventsMap.containsKey(toFile))
            throw new IllegalStateException(toFile + " " + ALREADY_EXISTS);

        FileEvent result = new FileEvent(toFile, sequence++, getPreviousEventForFile(fromFile));
        fileEventsMap.put(toFile, result);
        return result;

    }

    private FileEvent getPreviousEventForFile(String file) {
        return fileEventsMap.containsKey(file) ? fileEventsMap.get(file) : new FileEvent(file);
    }

    public FileEvent addFileMove(String fromFile, String toFile) {
        FileEvent result = new FileEvent(toFile, sequence++, getPreviousEventForFile(toFile), getPreviousEventForFile(fromFile));
        //fileEventsMap.put(toFile, result);
        return result;
    }
}
