package com.laffuste.ordo.properties.loaders;

import com.laffuste.ordo.properties.exception.PropertiesLoadingExpection;
import com.laffuste.ordo.properties.parsers.PropertiesFileParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads the default location for properties.
 */
@Slf4j
public class DefaultPropertiesLoader implements PropertiesLoader {

    private static final String DEFAULT_PROPERTIES_FILENAME = "app.properties";

    private final PropertiesFileParser fileLoader;

    public DefaultPropertiesLoader(PropertiesFileParser fileLoader) {
        this.fileLoader = fileLoader;
    }

    @Override
    public Properties load() {
        log.info("Loading default properties from file {}", DEFAULT_PROPERTIES_FILENAME);
        try {
            return findDefaultProperties();
        } catch (PropertiesLoadingExpection ex) {
            log.warn("Default properties file not found: {}", DEFAULT_PROPERTIES_FILENAME, ex);
            return new Properties();
        }
    }

    private Properties findDefaultProperties() throws PropertiesLoadingExpection {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILENAME)) {
            if (input == null) {
                throw new PropertiesLoadingExpection(DEFAULT_PROPERTIES_FILENAME);
            }
            return fileLoader.load(input);
        } catch (IOException e) {
            throw new PropertiesLoadingExpection(DEFAULT_PROPERTIES_FILENAME, e);
        }
    }

}
