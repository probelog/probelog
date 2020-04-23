package com.github.probelog;

import java.util.*;

public class PanelBuilder {

    private Map<String, String> introducedFileStates = new HashMap<>();
    private List<String> updatedFiles = new ArrayList<>();

    public void introduceFile(String file, String state) {
        introducedFileStates.put(file,state);
    }

    public void notifyFileChanged(String file) {
        updatedFiles.add(file);
    }

    public Set<Movement> getPanel(StateRetriever stateRetriever) {

        Set<Movement> result = new HashSet<>();
        for (String file : updatedFiles) {
            result.add(new Movement(file, introducedFileStates.get(file), stateRetriever.getStateForFile(file)));
        }
        return result;

    }

}
