package com.github.probelog;

import java.util.*;

public class ChangeMaker {

    private Map<String,FileChange> fileChanges = new HashMap();
    private Set<String> activeFiles = new HashSet();
    private int time=1;

    public Change makeChange() {
        List<FileChange> changes = new ArrayList<>();
        for(String activeFile: activeFiles)
            changes.add(fileChanges.get(activeFile));
        Collections.sort(changes, (o1, o2) -> o1.afterState().fileName().compareTo(o2.afterState().fileName()));
        Change result=changes.isEmpty() ? new NullChange(time) : changes.size()==1 ? changes.get(0) : new CompositeChange(changes);
        activeFiles.clear();
        time++;
        return result;
    }

    public void consumeCreate(String fileName) {
        activeFiles.add(fileName);
        fileChanges.put(fileName, new FileChange(time, new FileState(fileName)));
    }

    public void consumeUpdate(String fileName) {
        activeFiles.add(fileName);
        fileChanges.put(fileName, new FileChange(time, fileChanges.get(fileName), new FileState(fileName)));
    }

    public void consumeState(String fileName, String state) {
        fileChanges.get(fileName).afterState().setState(state);
    }
}
