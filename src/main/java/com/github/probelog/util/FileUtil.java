package com.github.probelog.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.*;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class FileUtil {

    private String destinationDir;

    public FileUtil(String destinationDir) {
        this.destinationDir=destinationDir;
    }

    public String copyToCheckSum(String fromFileName) {
        String checksum = checksum(fromFileName);
        copyFile(fromFileName, checksum);
        return checksum;
    }

    public File file(String checksum) {
        return new File(destinationDir + checksum);
    }

    private String checksum(String fileName) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(Files.readAllBytes(Paths.get(fileName)));
            byte[] digest = messageDigest.digest();
            return printHexBinary(digest);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void copyFile(String fromFileName, String checksum) {

        String toFileName = destinationDir + checksum;
        if (Files.exists(Paths.get(toFileName)))
            return;

        try {
            Files.copy(Paths.get(fromFileName), Paths.get(toFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> fileLines(String fileName) {

        List<String> result = new ArrayList<>();
        try (Stream<String> stream = Files.lines( Paths.get(destinationDir + fileName), UTF_8)) {
            stream.forEach(s -> result.add(s));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;

    }


}
