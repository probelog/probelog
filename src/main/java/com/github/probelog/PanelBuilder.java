package com.github.probelog;

import com.github.probelog.Movement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PanelBuilder {

    private Map<String, FromTo> fileStates = new HashMap<>();

    public void introduce(String file, String state) {
        fileStates.put(file,new FromTo(state));
    }

    public void position(String file, String state) {
        fileStates.get(file).to=state;
    }

    public Set<Movement> getPanel() {

        Set<Movement> result = new HashSet<>();
        for (String file : fileStates.keySet()) {
            FromTo fromTo = fileStates.get(file);
            result.add(new Movement(file, fromTo.from, fromTo.to));
        }
        return result;

    }

    private static class FromTo{

        String from, to;

        public FromTo(String from) {
            this.from = from;
        }

    }
}
