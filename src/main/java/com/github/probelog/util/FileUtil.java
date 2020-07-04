package com.github.probelog.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class FileUtil {

    public String copyToCheckSum(String fromFileName, String destinationDir) {
        String checksum = checksum(fromFileName);
        copyFile(fromFileName, destinationDir + checksum);
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

    private void copyFile(String fromFileName, String toFileName) {

        if (Files.exists(Paths.get(toFileName)))
            return;

        try {
            Files.copy(Paths.get(fromFileName), Paths.get(toFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
