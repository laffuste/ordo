package com.laffuste.ordo.properties.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import static java.util.Collections.emptyMap;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@UtilityClass
public class MapUtil {

    /**
     * Flattens a nested map into a shallow (single-level) one, merging the nested keys.
     *
     * @param keyMergeFunction merge function for nested maps
     */
    public static Map<String, Object> flattenMap(Map<String, Object> map,  BinaryOperator<String> keyMergeFunction) {
        if (map == null) {
            return emptyMap();
        }
        return flattenMap(map, keyMergeFunction, null);
    }

    private static Map<String, Object> flattenMap(Map<String, Object> map, BinaryOperator<String> keyMergeFunction, String propertyRoot) {
        Map<String, Object> flattenedMap = new HashMap<>();
        map.forEach((key, value) -> {
            if (isNotBlank(propertyRoot) && keyMergeFunction != null) {
                // merge keys
                key = keyMergeFunction.apply(propertyRoot, key);
            }
            if (value instanceof Map) {
                flattenedMap.putAll(flattenMap((Map<String, Object>) value, keyMergeFunction, key));
            } else {
                flattenedMap.put(key, value + "");  // force string conversion so the output if the exact same as java properties one
            }
        });
        return flattenedMap;
    }
}
