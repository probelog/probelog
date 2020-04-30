package com.github.probelog;

import java.util.HashMap;
import java.util.Map;

public class Linker {

    private FileEvent head;
    private Map<String, FileEvent> fileEventsMap = new HashMap<>();

    public FileEvent addFileUpdate(String file) {

        FileEvent previousEventForFile = fileEventsMap.containsKey(file) ? fileEventsMap.get(file) : createInitialState(file);
        head = new FileEvent(file, head, previousEventForFile!=null ? previousEventForFile : createInitialState(file));
        fileEventsMap.put(file, head);
        return head;

    }

    private static FileEvent createInitialState(String file) {
        return new FileEvent(file);
    }
}
