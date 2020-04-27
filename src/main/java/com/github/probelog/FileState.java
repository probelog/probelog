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

        Map<String, OrderedFileState> youngestVersionsMap = new HashMap<>();
        collectYoungestVersions(youngestVersionsMap, 1);
        return sortChronologically(youngestVersionsMap);

    }

    @NotNull
    private List<FileState> sortChronologically(Map<String, OrderedFileState> youngestVersionsMap) {
        List<OrderedFileState> orderedFileStates = new ArrayList<>(youngestVersionsMap.values());
        Collections.sort(orderedFileStates);
        List<FileState> result = new ArrayList<>();
        for (OrderedFileState orderedFileState : orderedFileStates)
            result.add(orderedFileState.fileState);
        return result;
    }

    private void collectYoungestVersions(Map<String, OrderedFileState> youngestVersionsMap, int order) {
        if (!youngestVersionsMap.containsKey(file))
            youngestVersionsMap.put(file, new OrderedFileState(order++, this));
        if (previous != null)
            previous.collectYoungestVersions(youngestVersionsMap, order);
    }

    private static class OrderedFileState implements Comparable<OrderedFileState> {

        Integer order;
        FileState fileState;

        public OrderedFileState(int order, FileState fileState) {
            this.order = order;
            this.fileState = fileState;
        }

        @Override
        public int compareTo(@NotNull OrderedFileState o) {
            return order.compareTo(o.order);
        }
    }
}
