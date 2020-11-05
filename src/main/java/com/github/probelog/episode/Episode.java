package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisode;

import java.util.List;

public interface Episode {


    enum Colour {
            GREEN,
            ORANGE,
            RED


    }
    enum Type {
        CODE_TAIL,
        RUN,
        JUMP,
        STUMBLE,
        STEP

    }

    Type type();

    Colour colour();

    boolean isRoot();

    Episode parent();

    String title();

    boolean hasLength();

    int length();

    Episode previous();

    Episode next();

    String description();

    FileChangeEpisode change();

    List<Episode> children();

    int failingTestRunsCount();

    int passingTestRunsCount();

    boolean hasChildren();

    String failDescription();

    boolean hasPrevious();

    boolean hasNext();

    String index();

}
