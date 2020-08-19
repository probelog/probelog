package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRunBuilder;

interface TestRunScript {
    void run(FileChangeEpisodeBuilder fileChangeEpisodeBuilder, TestRunBuilder testRunBuilder);
}
