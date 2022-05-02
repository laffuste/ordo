package com.laffuste.ordo.properties.parsers;

import com.laffuste.ordo.properties.exception.PropertiesFileNotFound;
import com.laffuste.ordo.properties.parsers.PropertiesFileParser;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.BinaryOperator;

import static com.laffuste.ordo.properties.utils.MapUtil.flattenMap;

public class YamlPropertyParser implements PropertiesFileParser {
    private static final BinaryOperator<String> nestedMapKeyMergeFunction = (k1, k2) -> k1 + "." + k2;

    private final Yaml yaml = new Yaml(); // not thread-safe, do not turn static

//    @Override
//    public Properties load(String filename) throws PropertiesFileNotFound {
//        Properties props = new Properties();
//        Map<String, Object> propsMap = loadMap(filename);
//
//        props.putAll(flattenMap(propsMap, nestedMapKeyMergeFunction));
//        return props;
//    }

    @Override
    public Properties load(InputStream input) throws PropertiesFileNotFound {
        Properties props = new Properties();
        Map<String, Object> propsMap;
        try {
            propsMap = yaml.load(input);
        } catch (Exception e) {
            throw new PropertiesFileNotFound("Couldn't parse properties", e);
        }
        props.putAll(flattenMap(propsMap, nestedMapKeyMergeFunction));
        return props;
    }

    @Override
    public boolean shouldTryToLoad(String filename) {
        return isYaml(filename);
    }

    private static boolean isYaml(String filename) {
        return filename.toLowerCase().endsWith("yaml") || filename.toLowerCase().endsWith("yml");
    }

//    private Map<String, Object> loadMap(String filename) throws PropertiesFileNotFound {
//        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
//            if (input == null) {
//                throw new PropertiesFileNotFound(filename);
//            }
//            return yaml.load(input);
//        } catch (IOException e) {
//            throw new PropertiesFileNotFound(filename, e);
//        }
//    }
//    @Override
//    private Map<String, Object> loadMap(InputStream input) {
////        if (input == null) {
////            throw new PropertiesFileNotFound();
////        }
//        return yaml.load(input);
//    }

}
