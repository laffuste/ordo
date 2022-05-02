package com.laffuste.ordo.properties.exception;

public class PropertiesLoadingExpection extends Exception {

    public PropertiesLoadingExpection(String msg, Throwable ex) {
        super(msg, ex);
    }

    public PropertiesLoadingExpection(String msg) {
        this(msg, null);
    }
}
