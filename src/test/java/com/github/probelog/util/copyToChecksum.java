package com.github.probelog.util;

import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.Charset.*;
import static org.junit.Assert.assertEquals;

public class copyToChecksum {

    @Test
    public void testCopyToCheckSum() throws IOException {

        String expectedCheckSum = "DD562DA33DDA879116D36CBB75042E7D";
        File copy = new File("src/test/resources/" + expectedCheckSum + ".probelog");
        copy.delete();

        File file = new File("src/test/resources/check-sum-test-file");
        String checksum  = new FileUtil("src/test/resources/").copyToCheckSum(file.getAbsolutePath());
        assertEquals(expectedCheckSum, checksum);
        assertEquals(Arrays.asList("line 1","line 2"),
                Files.readLines(copy, defaultCharset()));

        copy.delete();

    }


}
