// ConfigManager.java
package com.example.demo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            // Load default properties from classpath
            InputStream defaultProps = ConfigManager.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties");

            if (defaultProps != null) {
                properties.load(defaultProps);
                defaultProps.close();
            }

            // Load environment-specific properties if they exist
            String env = getEnvironment();
            InputStream envProps = ConfigManager.class
                    .getClassLoader()
                    .getResourceAsStream("application-" + env + ".properties");

            if (envProps != null) {
                properties.load(envProps);
                envProps.close();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    /**
     * Get configuration value with priority:
     * 1. Environment variable (highest priority)
     * 2. System property
     * 3. Properties file (lowest priority)
     */
    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, String defaultValue) {
        // 1. Check environment variable first
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        // 2. Check system property
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            return sysProp.trim();
        }

        // 3. Check properties file
        String propValue = properties.getProperty(key);
        if (propValue != null && !propValue.trim().isEmpty()) {
            return propValue.trim();
        }

        return defaultValue;
    }

    public static String getRequired(String key) {
        String value = get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Required configuration property '" + key + "' not found");
        }
        return value;
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null)
            return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null)
            return defaultValue;
        return Boolean.parseBoolean(value);
    }

    private static String getEnvironment() {
        return get("app.environment", "development");
    }

    public static boolean isDevelopment() {
        return "development".equals(getEnvironment());
    }

    public static boolean isProduction() {
        return "production".equals(getEnvironment());
    }
}