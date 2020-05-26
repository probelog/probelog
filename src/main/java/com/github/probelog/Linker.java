package com.github.probelog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.github.probelog.DiscardedNameUseException.illegalSource;

public class Linker {

    public static final String ALREADY_EXISTS = "already exists";
    public static final String NO_LONGER_EXISTS = "no longer exists";
    private int sequence=1;
    private Map<String, FileEvent> fileEventsMap = new HashMap<>();
    private Set<String> discardedNames = new HashSet<>();

    public FileEvent addFileUpdate(String file) {
        return addToFileEventMap(file, new FileEvent(file, sequence++, getPreviousEventForFile(file)));
    }

    public FileEvent addFileRename(String fromFile, String toFile) {

        checkFileExistence(toFile);
        return doMove(fromFile, toFile, ()-> new FileEvent(toFile, sequence++, getPreviousEventForFile(fromFile)));

    }

    public FileEvent addFileMove(String fromFile, String toFile) {
        return doMove(fromFile, toFile, ()-> new FileEvent(toFile, sequence++, getPreviousEventForFile(toFile), getPreviousEventForFile(fromFile)));
    }

    @NotNull
    private FileEvent doMove(String fromFile, String toFile, Callable<FileEvent> fileEventCreator) {
        checkFromFileNotDiscarded(fromFile, toFile);
        discardedNames.add(fromFile);
        try {
            return addToFileEventMap(toFile, fileEventCreator.call());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private void checkFromFileNotDiscarded(String fromFile, String toFile) {
        if (discardedNames.contains(fromFile))
            throw illegalSource(fromFile, toFile);
    }

    private void checkFileExistence(String file) {
        if (fileEventsMap.containsKey(file))
            throw new IllegalStateException(file + " " + ALREADY_EXISTS);
    }
}
