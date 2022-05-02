package com.laffuste.ordo.properties;

import com.laffuste.ordo.properties.loaders.PropertiesLoader;
import com.laffuste.ordo.properties.domain.TypedProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PropertyLoaderBuilderTest {

    @Test
    void load() {
        //given
        PropertiesLoader loader1 = mock(PropertiesLoader.class);
        Properties props1 = buildProperties("key1", "value1");
        when(loader1.load()).thenReturn(props1);

        PropertyLoaderBuilder<MyProperties> builder = PropertyLoaderBuilder.<MyProperties>builder()
                .add(loader1)
                .mapper(MyProperties::new);

        // when
        MyProperties props = builder.load();

        // then
        Properties p = props.properties;
        assertThat(p)
                .hasSize(1)
                .containsEntry("key1", "value1");
    }

    @Test
    void load_whenDifferentSources_expectMerge() {
        //given
        PropertiesLoader loader1 = mock(PropertiesLoader.class);
        Properties props1 = buildProperties("key1", "value1");
        when(loader1.load()).thenReturn(props1);

        PropertiesLoader loader2 = mock(PropertiesLoader.class);
        Properties props2 = buildProperties("key2", "value2");
        when(loader2.load()).thenReturn(props2);

        PropertyLoaderBuilder<MyProperties> builder = PropertyLoaderBuilder.<MyProperties>builder()
                .add(loader1)
                .add(loader2)
                .mapper(MyProperties::new);

        // when
        MyProperties props = builder.load();

        // then
        Properties p = props.properties;
        assertThat(p)
                .hasSize(2)
                .containsEntry("key1", "value1")
                .containsEntry("key2", "value2");
    }

    @Test
    void load_whenDifferentSources_whenCollision_expectOverride() {
        //given
        PropertiesLoader loader1 = mock(PropertiesLoader.class);
        Properties props1 = buildProperties("key1", "value1");
        when(loader1.load()).thenReturn(props1);

        PropertiesLoader loader2 = mock(PropertiesLoader.class);
        Properties props2 = buildProperties("key1", "value2");
        when(loader2.load()).thenReturn(props2);

        PropertyLoaderBuilder<MyProperties> builder = PropertyLoaderBuilder.<MyProperties>builder()
                .add(loader1)
                .add(loader2)
                .mapper(MyProperties::new);

        // when
        MyProperties props = builder.load();

        // then
        Properties p = props.properties;
        assertThat(p)
                .hasSize(1)
                .containsEntry("key1", "value2");
    }

    private static Properties buildProperties(String keys, String values) {
        Properties p = new Properties();
        p.put(keys, values);
        return p;
    }

    /**
     * Simple wrapper for testing
     */
    @RequiredArgsConstructor
    private static class MyProperties {
        private final TypedProperties properties;
    }
}