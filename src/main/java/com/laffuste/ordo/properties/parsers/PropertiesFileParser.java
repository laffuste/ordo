package com.laffuste.ordo.properties.parsers;

import com.laffuste.ordo.properties.exception.PropertiesLoadingExpection;

import java.io.InputStream;
import java.util.Properties;

public interface PropertiesFileParser {
    Properties load(InputStream input) throws PropertiesLoadingExpection;

    boolean shouldTryToLoad(String filename);
}
