package com.github.probelog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChangeMaker {

    private Change currentChange;
    private List<FileChange> fileChanges = new ArrayList<>();

    public Change makeChange() {
        Collections.sort(fileChanges, (o1, o2) -> o1.afterState().fileName().compareTo(o2.afterState().fileName()));
        currentChange=new Change(currentChange,fileChanges);
        return currentChange;
    }

    public void consumeCreate(String fileName) {
        fileChanges.add(new FileChange(new FileState(fileName)));
    }
}
