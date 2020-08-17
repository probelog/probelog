package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisode;

public interface Episode {
    Object description();

    boolean isRun();

    FileChangeEpisode change();

    boolean hasChildren();

    int failingTestRunsCount();

    int passingTestRunsCount();
}
