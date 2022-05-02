package com.laffuste.ordo.properties.domain;

import java.util.Properties;

import static java.lang.String.format;

/**
 * Adds functionality to java.util.Properties with typed getters.
 */
public class TypedProperties extends Properties {

    public TypedProperties(int initialCapacity) {
        super(initialCapacity);
    }

    public static TypedProperties ofProperties(Properties p) {
        TypedProperties typedProps = new TypedProperties(p.size());
        typedProps.putAll(p);
        return typedProps;
    }

    public Integer getInt(String propertyName) {
        Object ret = this.get(propertyName);
        if (ret instanceof Integer) {
            return (Integer) ret;
        }
        throw new IllegalArgumentException(format("Property %s is not an integer: %s", propertyName, ret));
    }

    public Double getDouble(String propertyName) {
        Object ret = this.get(propertyName);
        if (ret instanceof Double) {
            return (Double) ret;
        }
        throw new IllegalArgumentException(format("Property %s is not a double: %s", propertyName, ret));
    }

}
