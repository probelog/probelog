package com.github.probelog;

import java.util.ArrayList;
import java.util.List;

public class ChangeMaker {

    private Change currentChange;
    private List<FileState> fileStates = new ArrayList<>();

    public Change makeChange() {
        currentChange=new Change(currentChange, fileStates);
        return currentChange;
    }

    public void consumeCreate(String fileName) {
        fileStates.add(new FileState(fileName));
    }
}
