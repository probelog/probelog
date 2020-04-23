package com.github.probelog;


import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static java.util.Arrays.*;
import static org.junit.Assert.assertEquals;

public class PanelBuilderTest {

    @Test
    public void test() {

        PanelBuilder builder = new PanelBuilder();
        builder.introduceFile("file1","state1");
        builder.notifyFileChanged("file1");
        builder.introduceFile("file2","state3");
        builder.notifyFileChanged("file2");
        assertEquals(new HashSet(asList(new Movement("file1","state1","state2"),
                new Movement("file2","state3","state4"))),
                builder.getPanel(createStateRetriever(new FileState("file1", "state2"),
                        new FileState("file2", "state4"))));

    }

    private StubStateRetriever createStateRetriever(FileState... fileStates) {
        return new StubStateRetriever(fileStates);
    }

    private static class FileState {

        String file, state;

        public FileState(String file, String state) {
            this.file=file;
            this.state=state;
        }

    }

    private static class StubStateRetriever implements StateRetriever {

        Map<String, String> fileStateMap = new HashMap<>();

        public StubStateRetriever(FileState... fileStates ) {
            for(FileState fileState: fileStates)
                fileStateMap.put(fileState.file, fileState.state);
        }

        @Override
        public String getStateForFile(String file) {
            return fileStateMap.get(file);
        }
    }

}
