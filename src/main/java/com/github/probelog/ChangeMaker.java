package com.github.probelog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChangeMaker {

    private Change currentChange;
    private List<FileState> fileStates = new ArrayList<>();

    public Change makeChange() {
        Collections.sort(fileStates, (o1, o2) -> o1.fileName().compareTo(o2.fileName()));
        currentChange=new Change(currentChange,fileStates);
        return currentChange;
    }

    public void consumeCreate(String fileName) {
        fileStates.add(new FileState(fileName));
    }
}
