package com.github.probelog;

public class Linker {

    private FileEvent head;

    public FileEvent addFileUpdate(String fileA) {
        head = new FileEvent(head);
        return head;
    }
}
