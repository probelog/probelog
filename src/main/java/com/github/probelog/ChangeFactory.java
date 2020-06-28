package com.github.probelog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChangeFactory {

    public static FileChangeEpisode createChanges(AtomicFileChange fromAfterThis, AtomicFileChange upToAndIncludingThis) {
        assert(upToAndIncludingThis.isOrAfter(fromAfterThis));
        return new FileChangeEpisode(fromAfterThis, upToAndIncludingThis);
    }


}
