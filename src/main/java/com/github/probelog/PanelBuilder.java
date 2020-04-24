package com.github.probelog;

import java.util.*;

public class PanelBuilder {

    private Map<String, String> introducedFileStates = new HashMap<>();
    private Map<String, String> previousFileStates = new HashMap<>();
    private Set<String> updatedFiles = new HashSet<>();

    public void introduceFile(String file, String state) {
        introducedFileStates.put(file, state);
    }

    public void notifyFileChanged(String file) {
        updatedFiles.add(file);
    }

    public Set<Movement> getPanel(StateRetriever stateRetriever) {

        Set<Movement> result = new HashSet<>();
        for (String file : updatedFiles) {
            if (previousFileStates.containsKey(file))
                result.add(new Movement(file, previousFileStates.get(file), stateRetriever.getStateForFile(file)));
            else
                result.add(new Movement(file, introducedFileStates.get(file), stateRetriever.getStateForFile(file)));
        }
        for (Movement movement: result)
            previousFileStates.put(movement.getFile(), movement.getToState());
        updatedFiles.clear();
        return result;

    }


}
