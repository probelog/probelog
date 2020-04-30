package com.github.probelog;

public class Linker {

    private FileEvent head;

    public FileEvent addFileUpdate(String file) {

        FileEvent previousEventForFile = head==null ? null : head.findEventForFile(file);
        head = new FileEvent(file, head, previousEventForFile!=null ? previousEventForFile : createInitialState(file));
        return head;
    }

    private static FileEvent createInitialState(String file) {
        return new FileEvent(file);
    }
}
