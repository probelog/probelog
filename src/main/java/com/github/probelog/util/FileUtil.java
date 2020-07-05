package com.github.probelog.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

        String toFileName = destinationDir + checksum + ".probelog";
        if (Files.exists(Paths.get(toFileName)))
            return;

        try {
            Files.copy(Paths.get(fromFileName), Paths.get(toFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
