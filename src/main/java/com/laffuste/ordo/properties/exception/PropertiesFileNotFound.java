package com.laffuste.ordo.properties.exception;

public class PropertiesFileNotFound extends Exception {

    public PropertiesFileNotFound(String msg, Throwable ex) {
        super(msg, ex);
    }

    public PropertiesFileNotFound(String msg) {
        this(msg, null);
    }
}
