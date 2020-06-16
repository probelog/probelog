package com.github.probelog;

import java.util.List;

public interface IDevEvent {

    void collectDescription(List<String> lines);
    State state();
    String fileName();
    String fileValue();
    IDevEvent previous();
    IDevEvent previousSibling();
    IDevEvent previousSibling(IDevEvent episodeStart);
    IDevEvent findPrevious(String fileName);
    IDevEvent findPreviousBoforeEpisodeStart(String fileName, IDevEvent episodeStart);
}
