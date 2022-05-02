package com.laffuste.ordo.properties.parsers;

import com.laffuste.ordo.properties.exception.PropertiesFileNotFound;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JavaPropertyParser implements PropertiesFileParser {

    @Override
    public Properties load(InputStream input) throws PropertiesFileNotFound {
        Properties appProps = new Properties();
        try {
            appProps.load(input);
        } catch (IOException e) {
            throw new PropertiesFileNotFound("Coulnd't load", e);
        }
        return appProps;
    }

//    @Override
//    public Properties load(String filename) throws PropertiesFileNotFound {
//        Properties appProps = new Properties();
//        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
//            if (input == null) {
//                throw new PropertiesFileNotFound(filename);
//            }
//            appProps.load(input);
//        } catch (IOException e) {
//            throw new PropertiesFileNotFound(filename, e);
//        }
//        return appProps;
//    }

    @Override
    public boolean shouldTryToLoad(String filename) {
        return true;
    }

}
