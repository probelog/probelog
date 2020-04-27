package com.github.probelog;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FileState {

    private String file;
    private FileState previous;

    public FileState(String file) {
        this.file = file;
    }

    public void setPrevious(FileState previous) {
        this.previous = previous;
    }

    public List<FileState> findLatestVersions() {

        LinkedHashMap<String, FileState> youngestVersionsMap = new LinkedHashMap<>();
        collectYoungestVersions(youngestVersionsMap, 1);
        return new ArrayList<>(youngestVersionsMap.values());

    }

    private void collectYoungestVersions(Map<String, FileState> youngestVersionsMap, int order) {
        if (!youngestVersionsMap.containsKey(file))
            youngestVersionsMap.put(file, this);
        if (previous != null)
            previous.collectYoungestVersions(youngestVersionsMap, order);
    }

}
