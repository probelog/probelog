package com.github.probelog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Linker {

    public static final String ALREADY_EXISTS = "already exists";
    private int sequence=1;
    private Map<String, FileEvent> fileEventsMap = new HashMap<>();

    public FileEvent addFileUpdate(String file) {

        return addToFileEventMap(file, new FileEvent(file, sequence++, getPreviousEventForFile(file)));

    }

    public FileEvent addFileRename(String fromFile, String toFile) {

        checkFileExistence(toFile);
        return addToFileEventMap(toFile, new FileEvent(toFile, sequence++, getPreviousEventForFile(fromFile)));

    }

    public FileEvent addFileMove(String fromFile, String toFile) {
        return addToFileEventMap(toFile, new FileEvent(toFile, sequence++, getPreviousEventForFile(toFile), getPreviousEventForFile(fromFile)));
    }

    public FileEvent addFileCreate(String file) {
        checkFileExistence(file);
        return addToFileEventMap(file, new FileEvent(file, sequence++, null));
    }

    private FileEvent getPreviousEventForFile(String file) {
        return fileEventsMap.containsKey(file) ? fileEventsMap.get(file) : new FileEvent(file);
    }

    private FileEvent addToFileEventMap(String file, FileEvent fileEvent) {
        fileEventsMap.put(file, fileEvent);
        return fileEvent;
    }

    private void checkFileExistence(String file) {
        if (fileEventsMap.containsKey(file))
            throw new IllegalStateException(file + " " + ALREADY_EXISTS);
    }
}
