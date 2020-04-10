package com.ake3m.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProvider {

    public static Properties getProperties() {

        var properties = new Properties();
        var defaultProperties = getProperties("application.properties");
        var localProperties = getProperties("application.local.properties");
        properties.putAll(defaultProperties);
        properties.putAll(localProperties);

        return properties;
    }

    private static Properties getProperties(String url) {

        var properties = new Properties();

        try (InputStream inputStream = ConfigProvider.class.getClassLoader().getResourceAsStream(url)) {


            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
