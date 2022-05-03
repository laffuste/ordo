package com.laffuste.ordo.properties.domain;

import java.util.Properties;

import static java.lang.String.format;

/**
 * Adds functionality to java.util.Properties with typed getters.
 */
public class TypedProperties extends Properties {

    public TypedProperties() {
        super();
    }

    public static TypedProperties ofProperties(Properties p) {
        TypedProperties typedProps = new TypedProperties();
        typedProps.putAll(p);
        return typedProps;
    }

    public Integer getInt(String propertyName) {
        Object ret = this.get(propertyName);
        if (ret instanceof Integer) {
            return (Integer) ret;
        }
        if (ret instanceof String) {
            try {
                return Integer.valueOf((String) ret);
            } catch (NumberFormatException e) {
                // doesn't matter
            }
        }
        throw new IllegalArgumentException(format("Property %s is not an integer: %s", propertyName, ret));
    }

    public Double getDouble(String propertyName) {
        Object ret = this.get(propertyName);
        if (ret instanceof Double) {
            return (Double) ret;
        }
        if (ret instanceof Integer) {
            return ((Integer) ret).doubleValue();
        }
        if (ret instanceof String) {
            try {
                return Double.valueOf((String) ret);
            } catch (NumberFormatException e) {
                // doesn't matter
            }
        }
        throw new IllegalArgumentException(format("Property %s is not a double: %s", propertyName, ret));
    }

}
