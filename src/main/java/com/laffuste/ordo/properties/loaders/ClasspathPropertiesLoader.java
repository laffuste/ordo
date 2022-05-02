package com.laffuste.ordo.properties.loaders;

import com.laffuste.ordo.properties.exception.PropertiesFileNotFound;
import com.laffuste.ordo.properties.parsers.PropertiesFileParser;
import com.laffuste.ordo.properties.loaders.PropertiesLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
public class ClasspathPropertiesLoader implements PropertiesLoader {

    private final String configFileJvmArg;

    private final PropertiesFileParser[] fileLoaders;

    public ClasspathPropertiesLoader(String configFileJvmArg, PropertiesFileParser... fileLoaders) {
        this.configFileJvmArg = configFileJvmArg;
        this.fileLoaders = fileLoaders;
    }

    @Override
    public Properties load() {
        String configFile  = System.getProperty(configFileJvmArg);
        if (isBlank(configFile)) {
           log.debug("no config file supplied by jvm arg");
           return new Properties();
        }

        try {
            return findPropertiesByFileName(configFile);
        } catch (PropertiesFileNotFound ex) {
            log.warn("Properties file not found: {}", configFile, ex);
            return new Properties();
        }
    }

    private Properties findPropertiesByFileName(String configFile) throws PropertiesFileNotFound {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new PropertiesFileNotFound(configFile);
            }
            for (PropertiesFileParser loader : fileLoaders) {
                if (loader.shouldTryToLoad(configFile)) {
                    return loader.load(input); // stops at first success
                }
            }
        } catch (IOException e) {
            throw new PropertiesFileNotFound(configFile, e);
        }
        return new Properties();
    }
}
