package io.github.bunmo.file.common.util;

public class FilePathGenerator {

    public static String generateProfileFilePath(String uuid, Long timestamp) {
        return uuid + "/profile/" + timestamp;
    }
}
