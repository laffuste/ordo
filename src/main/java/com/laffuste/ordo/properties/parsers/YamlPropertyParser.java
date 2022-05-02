package com.laffuste.ordo.properties.parsers;

import com.laffuste.ordo.properties.exception.PropertiesLoadingExpection;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.BinaryOperator;

import static com.laffuste.ordo.properties.utils.MapUtil.flattenMap;

public class YamlPropertyParser implements PropertiesFileParser {
    private static final BinaryOperator<String> nestedMapKeyMergeFunction = (k1, k2) -> k1 + "." + k2;
    private final Yaml yaml = new Yaml(); // not thread-safe, do not turn static

    @Override
    public Properties load(InputStream input) throws PropertiesLoadingExpection {
        Properties props = new Properties();
        Map<String, Object> propsMap;
        try {
            propsMap = yaml.load(input);
        } catch (Exception e) {
            throw new PropertiesLoadingExpection("Couldn't parse properties", e);
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

}
