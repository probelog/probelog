package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisode;

import java.util.List;

public interface Episode {
    Object description();

    boolean isRun();

    FileChangeEpisode change();

    boolean hasChildren();

    List<Episode> children();

    int failingTestRunsCount();

    int passingTestRunsCount();
}
