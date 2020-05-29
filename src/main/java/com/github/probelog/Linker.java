package com.github.probelog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.github.probelog.ActiveFileException.illegalCreate;
import static com.github.probelog.ActiveFileException.illegalMoveCreate;
import static com.github.probelog.DiscardedNameUseException.*;

public class Linker {

    private int sequence=1;
    private Map<String, FileEvent> activeEvents = new HashMap<>();
    private Map<String,FileEvent> discardEvents = new HashMap<>();

    public FileEvent addFileUpdate(String file) {
        checkFileNotDiscarded(file, ()->illegalUpdate(file));
        return addActiveEvent(file, new FileEvent(file, sequence++, getPreviousEventForFile(file)));
    }

    public FileEvent addFileMoveCreate(String fromFile, String toFile) {

        checkFileNotExisting(toFile, ()-> illegalMoveCreate(fromFile, toFile));
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
        discardFile(fromFile, moveEvent);
        return addActiveEvent(toFile, moveEvent);

    }

    public FileEvent addFileCreate(String file) {
        checkFileNotExisting(file, ()->illegalCreate(file));
        return addActiveEvent(file, new FileEvent(file, sequence++, recycleDiscarded(file)));
    }

    private FileEvent getPreviousEventForFile(String file) {
        return activeEvents.containsKey(file) ? activeEvents.get(file) : new FileEvent(file);
    }

    private FileEvent addActiveEvent(String file, FileEvent fileEvent) {
        activeEvents.put(file, fileEvent);
        return fileEvent;
    }

    private void discardFile(String fromFile, FileEvent moveEvent) {
        discardEvents.put(fromFile, moveEvent);
        activeEvents.remove(fromFile);
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

    private void checkFileNotExisting(String file, Runnable exceptionThrower) {
        if (activeEvents.containsKey(file))
            exceptionThrower.run();
    }

    private boolean isDiscardedName(String file) {
        return discardEvents.containsKey(file);
    }

}
