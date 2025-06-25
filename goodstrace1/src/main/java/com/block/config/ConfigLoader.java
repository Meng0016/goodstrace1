package com.block.config;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
@Component
public class ConfigLoader {
    private static String webBASEUrl;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("application.yml")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.yml");
            }
            properties.load(input);
            webBASEUrl = properties.getProperty("webase.front.url");
        } catch (IOException ex) {
            throw new RuntimeException("Error loading configuration", ex);
        }
    }

    public static String getWebASEUrl() { return webBASEUrl; }
}
