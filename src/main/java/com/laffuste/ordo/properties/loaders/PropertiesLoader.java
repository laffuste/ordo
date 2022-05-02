package com.laffuste.ordo.properties.loaders;

import java.util.Properties;

@FunctionalInterface
public interface PropertiesLoader {
    Properties load();
}
