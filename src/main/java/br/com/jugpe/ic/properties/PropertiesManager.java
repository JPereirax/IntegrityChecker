package br.com.jugpe.ic.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {

    public Properties getProperties() throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        FileInputStream file = new FileInputStream(classLoader.getResource("application.properties").getFile());
        properties.load(file);
        return properties;
    }

}