package com.pm.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtil {

    private static final Charset WIN_1251 = Charset.forName("windows-1251");

    public static List<String> readLines(Path filePath) throws IOException {
        return Files.readAllLines(filePath, WIN_1251);
    }

}
