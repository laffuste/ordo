package com.laffuste.ordo.properties.loaders;

import com.laffuste.ordo.properties.exception.PropertiesLoadingExpection;
import com.laffuste.ordo.properties.parsers.PropertiesFileParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static org.apache.logging.log4j.util.Strings.isBlank;

/**
 * Loads properties supplied by args from the classpath.
 */
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
           log.info("No config file supplied by jvm arg");
           return new Properties();
        }

        log.info("Attempting to load {}", configFile);
        if (log.isDebugEnabled()) {
            printClassPath();
        }
        try {
            return findPropertiesByFileName(configFile);
        } catch (PropertiesLoadingExpection ex) {
            log.warn("Properties file not found: {}", configFile);
            return new Properties();
        }
    }
    private Properties findPropertiesByFileName(String configFile) throws PropertiesLoadingExpection {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new PropertiesLoadingExpection(configFile);
            }
            for (PropertiesFileParser loader : fileLoaders) {
                if (loader.shouldTryToLoad(configFile)) {
                    return loader.load(input); // stops at first success
                }
            }
        } catch (IOException e) {
            throw new PropertiesLoadingExpection(configFile, e);
        }
        return new Properties();
    }

    private void printClassPath() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        String classpaths = Arrays.stream(urls)
                .map(URL::getFile)
                .collect(joining(", "));
        log.debug("Searching in classpaths: {}", classpaths);
    }
}
