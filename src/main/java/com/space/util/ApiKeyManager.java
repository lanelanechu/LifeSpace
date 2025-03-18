package com.space.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiKeyManager {
    private static final String CONFIG_FILE = "config/api-keys.properties";
    private static Properties properties;
    
    static {
        properties = new Properties();
        try (InputStream inputStream = ApiKeyManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println("無法找到 API 金鑰配置檔案");
            }
        } catch (IOException e) {
            System.err.println("無法載入 API 金鑰配置檔案: " + e.getMessage());
        }
    }
    
    public static String getGoogleMapsApiKey() {
        return properties.getProperty("google.maps.api.key");
    }
}
