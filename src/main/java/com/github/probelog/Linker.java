package com.github.probelog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        return doMove(fromFile, toFile, recycleDiscarded(toFile));

    }

    public FileEvent addFileMoveUpdate(String fromFile, String toFile) {
        checkFileNotDiscarded(toFile, ()->illegalMoveUpdate(fromFile, toFile));
        return doMove(fromFile, toFile,  getPreviousEventForFile(toFile));
    }

    @NotNull
    private FileEvent doMove(String fromFile, String toFile, FileEvent previousEvent) {

        checkFileNotDiscarded(fromFile, ()->illegalSource(fromFile, toFile));
        FileEvent moveEvent = new FileEvent(toFile, sequence++, previousEvent, getPreviousEventForFile(fromFile));
        saveDiscardEvent(fromFile, moveEvent);
        return addToFileEventMap(toFile, moveEvent);

    }

    public FileEvent addFileCreate(String file) {
        checkFileExistence(file, ()->illegalCreate(file));
        return addToFileEventMap(file, new FileEvent(file, sequence++, recycleDiscarded(file)));
    }

    private FileEvent getPreviousEventForFile(String file) {
        return fileEventsMap.containsKey(file) ? fileEventsMap.get(file) : new FileEvent(file);
    }

    private FileEvent addToFileEventMap(String file, FileEvent fileEvent) {
        fileEventsMap.put(file, fileEvent);
        return fileEvent;
    }

    private void saveDiscardEvent(String fromFile, FileEvent moveEvent) {
        discardEvents.put(fromFile, moveEvent);
    }

    @Nullable
    private FileEvent recycleDiscarded(String file) {
        if (!isDiscardedName(file))
            return null;
        FileEvent previous =  discardEvents.get(file);
        discardEvents.remove(file);
        return previous;
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
