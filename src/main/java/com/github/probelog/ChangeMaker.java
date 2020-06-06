package com.github.probelog;

import java.util.*;

public class ChangeMaker {

    private Change currentChange;
    private Map<String,FileChange> fileChanges = new HashMap();
    private Set<String> activeFiles = new HashSet();

    public Change makeChange() {
        List<FileChange> changes = new ArrayList<>();
        for(String activeFile: activeFiles)
            changes.add(fileChanges.get(activeFile));
        Collections.sort(changes, (o1, o2) -> o1.afterState().fileName().compareTo(o2.afterState().fileName()));
        currentChange=new Change(currentChange,changes);
        activeFiles.clear();
        return currentChange;
    }

    public void consumeCreate(String fileName) {
        activeFiles.add(fileName);
        fileChanges.put(fileName, new FileChange(new FileState(fileName)));
    }

    public void consumeUpdate(String fileName) {
        activeFiles.add(fileName);
        fileChanges.put(fileName, new FileChange(fileChanges.get(fileName).afterState(), new FileState(fileName)));
    }

    public void consumeState(String fileName, String state) {
        fileChanges.get(fileName).afterState().setState(state);
    }
}
