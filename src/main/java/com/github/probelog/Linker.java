package com.github.probelog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.github.probelog.ActiveFileException.illegalCreate;
import static com.github.probelog.ActiveFileException.illegalRename;
import static com.github.probelog.DiscardedNameUseException.*;

public class Linker {

    private int sequence=1;
    private Map<String, FileEvent> fileEventsMap = new HashMap<>();
    private Map<String,FileEvent> discardEvents = new HashMap<>();

    public FileEvent addFileUpdate(String file) {
        checkFileNotDiscarded(file, ()->illegalUpdate(file));
        return addToFileEventMap(file, new FileEvent(file, sequence++, getPreviousEventForFile(file)));
    }

    public FileEvent addFileRename(String fromFile, String toFile) {

        checkFileExistence(toFile, ()->illegalRename(fromFile, toFile));
        return doMove(fromFile, toFile, ()-> new FileEvent(toFile, sequence++, getPreviousEventForFile(fromFile)));

    }

    public FileEvent addFileMove(String fromFile, String toFile) {
        return doMove(fromFile, toFile, ()-> new FileEvent(toFile, sequence++, getPreviousEventForFile(toFile), getPreviousEventForFile(fromFile)));
    }

    @NotNull
    private FileEvent doMove(String fromFile, String toFile, Callable<FileEvent> fileEventCreator) {

        checkFileNotDiscarded(fromFile, ()->illegalSource(fromFile, toFile));
        FileEvent moveEvent;
        try {
            moveEvent = fileEventCreator.call();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        discardEvents.put(fromFile, moveEvent);
        return addToFileEventMap(toFile, moveEvent);

    }

    public FileEvent addFileCreate(String file) {
        checkFileExistence(file, ()->illegalCreate(file));
        return addToFileEventMap(file, new FileEvent(file, sequence++,  isDiscardedName(file) ? discardEvents.get(file) : null));
    }

    private FileEvent getPreviousEventForFile(String file) {
        return fileEventsMap.containsKey(file) ? fileEventsMap.get(file) : new FileEvent(file);
    }

    private FileEvent addToFileEventMap(String file, FileEvent fileEvent) {
        fileEventsMap.put(file, fileEvent);
        return fileEvent;
    }

    private void checkFileNotDiscarded(String file, Runnable exceptionThrower) {
        if (isDiscardedName(file))
            exceptionThrower.run();
    }

    private void checkFileExistence(String file, Runnable exceptionThrower) {
        if (!isDiscardedName(file) && fileEventsMap.containsKey(file))
            exceptionThrower.run();
    }

    private boolean isDiscardedName(String file) {
        return discardEvents.containsKey(file);
    }
}
