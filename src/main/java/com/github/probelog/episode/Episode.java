package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisode;

import java.util.List;

public interface Episode {

    enum Type {
        RUN,
        STAGGER,
        TEST;
    }

    Type type();

    Object description();

    FileChangeEpisode change();

    boolean hasChildren();

    List<Episode> children();

    int failingTestRunsCount();

    int passingTestRunsCount();
}
