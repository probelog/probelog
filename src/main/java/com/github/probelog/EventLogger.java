package com.github.probelog;

import java.util.HashMap;
import java.util.Map;

import static com.github.probelog.State.*;

public class EventLogger {

    private Map<String, State> fileStatesMap = new HashMap<>();

    public State state(String fileName) {
        return fileStatesMap.getOrDefault(fileName, UNKNOWN);
    }

    public void logCreate(String fileName) {
        fileStatesMap.put(fileName, CREATED);
    }

    public void logInitialize(String fileName, String fileContent) {
        fileStatesMap.put(fileName, INITIALIZED);
    }
}
