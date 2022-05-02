package com.laffuste.ordo.properties.loaders;

import com.laffuste.ordo.properties.exception.PropertiesFileNotFound;
import com.laffuste.ordo.properties.parsers.YamlPropertyParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class YamlPropertyLoaderTest {

    private final YamlPropertyParser loader = new YamlPropertyParser();

    @Test
    public void load() throws PropertiesFileNotFound, IOException {
        // given
        String filename = "app-properties.yaml";

        // when
        Properties props;
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            props = loader.load(input);
        }

        // then
        assertThat(props)
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("property-a", "100")
                .containsEntry("test-properties.property-b", "200")
                .containsEntry("test-properties.property-c", "300");
    }

//    @Test
//    public void load_whenFileNotFound() throws PropertiesFileNotFound {
//        // given
//        String filename = "non-existing.properties";
//
//        // when
//        Throwable t = catchThrowable(() ->loader.load(filename));
//
//        // then
//        assertThat(t)
//                .isInstanceOf(PropertiesFileNotFound.class)
//                .hasMessage("Properties file non-existing.properties not found");
//    }

    @Test
    void load_whenNotYaml_expectError() throws PropertiesFileNotFound, IOException {
        // given
        String filename = "app.properties";

        // when
        Throwable t;
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            t = catchThrowable(() -> loader.load(input));
        }

        // then
        assertThat(t)
                .isInstanceOf(PropertiesFileNotFound.class)
                .hasMessage("Couldn't parse properties");
    }

}