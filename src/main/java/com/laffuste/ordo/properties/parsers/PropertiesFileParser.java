package com.laffuste.ordo.properties.parsers;

import com.laffuste.ordo.properties.exception.PropertiesFileNotFound;

import java.io.InputStream;
import java.util.Properties;

public interface PropertiesFileParser {
    Properties load(InputStream input) throws PropertiesFileNotFound;
    boolean shouldTryToLoad(String filename);
}
