package com.github.probelog;

public class Linker {

    private FileEvent head;

    public FileEvent addFileUpdate(String file) {
        head = new FileEvent(file, head);
        return head;
    }
}
