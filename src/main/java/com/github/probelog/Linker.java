package com.github.probelog;

import java.util.HashMap;
import java.util.Map;

public class Linker {

    private int sequence=1;
    private Map<String, FileEvent> fileEventsMap = new HashMap<>();

    public FileEvent addFileUpdate(String file) {

        FileEvent result = new FileEvent(sequence++, getPreviousEventForFile(file));
        fileEventsMap.put(file, result);
        return result;

    }

    public FileEvent addFileRename(String fromFile, String toFile) {

        FileEvent result = new FileEvent(sequence++, getPreviousEventForFile(fromFile));
        fileEventsMap.put(toFile, result);
        return result;

    }

    private FileEvent getPreviousEventForFile(String file) {
        return fileEventsMap.containsKey(file) ? fileEventsMap.get(file) : new FileEvent();
    }

}
