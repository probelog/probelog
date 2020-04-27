package com.github.probelog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileState {

    private String file;
    private FileState previous;

    public FileState(String file) {
        this.file=file;
    }

    public void setPrevious(FileState previous) {
        this.previous=previous;
    }

    public Set<FileState> findYoungestVersions() {
        Map<String, FileState> youngestVersionsMap = new HashMap<>();
        collectYoungestVersions(youngestVersionsMap);
        return new HashSet<>(youngestVersionsMap.values());
    }

    private void collectYoungestVersions(Map<String, FileState> youngestVersionsMap) {
        if (!youngestVersionsMap.containsKey(file))
            youngestVersionsMap.put(file,this);
        if (previous!=null)
            previous.collectYoungestVersions(youngestVersionsMap);
    }
}
