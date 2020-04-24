package com.github.probelog;

import java.util.*;

public class PanelBuilder {

    private Map<String, String> introducedFileStates = new HashMap<>();
    private Set<String> updatedFiles = new HashSet<>();
    private Set<Movement> previousPanel;

    public void introduceFile(String file, String state) {
        introducedFileStates.put(file, state);
    }

    public void notifyFileChanged(String file) {
        updatedFiles.add(file);
    }

    public Set<Movement> getPanel(StateRetriever stateRetriever) {

        Set<Movement> result = new HashSet<>();
        for (String file : updatedFiles) {
            if (previousMovement(file)!=null)
                result.add(new Movement(file, previousMovement(file).getToState(), stateRetriever.getStateForFile(file)));
            else
                result.add(new Movement(file, introducedFileStates.get(file), stateRetriever.getStateForFile(file)));
        }
        previousPanel = result;
        updatedFiles.clear();
        return result;

    }

    Movement previousMovement(String file) {
        if (previousPanel==null)
            return null;
        for (Movement movement : previousPanel) {
            if (movement.getFile().equals(file))
                return movement;
        }
        return null;
    }

}
