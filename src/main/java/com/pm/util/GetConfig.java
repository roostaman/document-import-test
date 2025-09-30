package com.pm.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class GetConfig {

    private static final Properties properties = new Properties();
    private static final String ENV_PROPS_PATH = "src/main/resources/env.properties";

    static  {
        try (InputStream inputStream = Files.newInputStream(Path.of(ENV_PROPS_PATH))) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties file: " + ENV_PROPS_PATH, e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
