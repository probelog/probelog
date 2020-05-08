package com.github.probelog;

import java.util.HashMap;
import java.util.Map;

public class Linker {

    private int sequence=1;
    private Map<String, FileEvent> fileEventsMap = new HashMap<>();

    public FileEvent addFileUpdate(String file) {

        FileEvent previousEventForFile = fileEventsMap.containsKey(file) ? fileEventsMap.get(file) : createInitialState(file);
        FileEvent result = new FileEvent(sequence++, previousEventForFile!=null ? previousEventForFile : createInitialState(file));
        fileEventsMap.put(file, result);
        return result;

    }

    private static FileEvent createInitialState(String file) {
        return new FileEvent();
    }

    public FileEvent addFileRename(String fromFile, String toFile) {
        FileEvent result = new FileEvent(sequence++, fileEventsMap.get(fromFile));
        fileEventsMap.put(toFile, result);
        return result;
    }
}
