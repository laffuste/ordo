package com.laffuste.ordo.properties.parsers;

import com.laffuste.ordo.properties.exception.PropertiesLoadingExpection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JavaPropertyParser implements PropertiesFileParser {

    @Override
    public Properties load(InputStream input) throws PropertiesLoadingExpection {
        Properties appProps = new Properties();
        try {
            appProps.load(input);
        } catch (IOException e) {
            throw new PropertiesLoadingExpection("Couldn't load", e);
        }
        return appProps;
    }

    @Override
    public boolean shouldTryToLoad(String filename) {
        return true;
    }

}
