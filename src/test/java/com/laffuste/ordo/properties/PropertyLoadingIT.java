package com.laffuste.ordo.properties;

import com.laffuste.ordo.properties.loaders.ClasspathPropertiesLoader;
import com.laffuste.ordo.properties.loaders.DefaultPropertiesLoader;
import com.laffuste.ordo.properties.parsers.JavaPropertyParser;
import com.laffuste.ordo.properties.parsers.PropertiesFileParser;
import com.laffuste.ordo.properties.parsers.YamlPropertyParser;
import com.laffuste.ordo.properties.domain.TypedProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyLoadingIT {

    @Test
    void load_allTypes() {
        //given
        PropertiesFileParser javaPropertyLoader = new JavaPropertyParser();
        PropertiesFileParser yamlPropertyLoader = new YamlPropertyParser();
        PropertyLoaderBuilder<MyProperties> builder = PropertyLoaderBuilder.<MyProperties>builder()
                .add(new DefaultPropertiesLoader(javaPropertyLoader))
                .add(new ClasspathPropertiesLoader("app.config", yamlPropertyLoader, javaPropertyLoader))
                .mapper(MyProperties::new);
//        System.setProperty("app.config", "src/test/resources/other-properties.yaml");
        System.setProperty("app.config", "other-properties.yaml");

        // when
        MyProperties props = builder.load();

        // then
        Properties p = props.properties;
        assertThat(p)
                .hasSize(5)
                // from default location
                .containsEntry("property-a", "100")
                .containsEntry("test-properties.property-b", "200")
                .containsEntry("test-properties.property-c", "300")
                // from jvm arg yaml
                .containsEntry("other-properties.new-prop", "a new value")
                .containsEntry("validator.quantity.limit", "100001");
    }

    /**
     * Simple wrapper for testing
     */
    @RequiredArgsConstructor
    private static class MyProperties {
        private final TypedProperties properties;
    }
}