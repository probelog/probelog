package com.github.probelog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.github.probelog.ActiveFileException.illegalCreate;
import static com.github.probelog.ActiveFileException.illegalMoveCreate;
import static com.github.probelog.DiscardedNameUseException.*;

public class Linker {

    private int sequence=1;
    private Map<String, FileEvent> fileEventsMap = new HashMap<>();
    private Map<String,FileEvent> discardEvents = new HashMap<>();

    public FileEvent addFileUpdate(String file) {
        checkFileNotDiscarded(file, ()->illegalUpdate(file));
        return addToFileEventMap(file, new FileEvent(file, sequence++, getPreviousEventForFile(file)));
    }

    public FileEvent addFileMoveCreate(String fromFile, String toFile) {

        checkFileExistence(toFile, ()-> illegalMoveCreate(fromFile, toFile));
        return doMove(fromFile, toFile, ()-> new FileEvent(toFile, sequence++, discardEvents.get(toFile), getPreviousEventForFile(fromFile)));

    }

    public FileEvent addFileMoveUpdate(String fromFile, String toFile) {
        checkFileNotDiscarded(toFile, ()->illegalMoveUpdate(fromFile, toFile));
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
        FileEvent previous = isDiscardedName(file) ? discardEvents.get(file) : null;
        if (isDiscardedName(file))
            discardEvents.remove(file);
        return addToFileEventMap(file, new FileEvent(file, sequence++,  previous));
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
