package com.github.probelog;

import java.util.*;

public class PanelBuilder {

    private Map<String, String> introducedFileStates = new HashMap<>();
    private Map<String, String> previousFileStates = new HashMap<>();
    private Set<String> updatedFiles = new HashSet<>();
    private Panel previous;

    public void introduceFile(String file, String state) {
        introducedFileStates.put(file, state);
    }

    public void notifyFileChanged(String file) {
        updatedFiles.add(file);
    }

    public Panel getPanel(StateRetriever stateRetriever) {

        Set<Movement> movements = new HashSet<>();
        for (String file : updatedFiles) {
            if (previousFileStates.containsKey(file))
                movements.add(new Movement(file, previousFileStates.get(file), stateRetriever.getStateForFile(file)));
            else
                movements.add(new Movement(file, introducedFileStates.get(file), stateRetriever.getStateForFile(file)));
        }
        for (Movement movement: movements)
            previousFileStates.put(movement.getFile(), movement.getToState());
        updatedFiles.clear();
        Panel result = new Panel(previous, movements);
        previous=result;
        return result;

    }


}
