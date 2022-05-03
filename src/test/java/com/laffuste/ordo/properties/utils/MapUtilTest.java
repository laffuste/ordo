package com.laffuste.ordo.properties.utils;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.BinaryOperator;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

class MapUtilTest {

    private static final BinaryOperator<String> DEFAULT_MERGE_FUNC = (k1, k2) -> k1 + "." + k2;

    @Test
    public void flattenMap_whenNull() {
        Map<String, Object> nestedMap = null;

        // when
        Map<String, Object> props = MapUtil.flattenMap(nestedMap, DEFAULT_MERGE_FUNC);

        // then
        assertThat(props)
                .isEmpty();  // check mapping is null safe
    }

    @Test
    public void flattenMap_whenEmpty() {
        Map<String, Object> nestedMap = emptyMap();

        // when
        Map<String, Object> props = MapUtil.flattenMap(nestedMap, DEFAULT_MERGE_FUNC);

        // then
        assertThat(props)
                .isEmpty();
    }

    @Test
    public void flattenMap_whenAlreadyFlat() {
        Map<String, Object> nestedMap = ImmutableMap.of(
                "property-a", 100,
                "property-b", 200
        );

        // when
        Map<String, Object> props = MapUtil.flattenMap(nestedMap, DEFAULT_MERGE_FUNC);

        // then
        assertThat(props)
                .isNotEmpty()
                .hasSize(2)
                .containsEntry("property-a", "100")
                .containsEntry("property-b", "200");
    }

    @Test
    public void flattenMap_with2Levels() {
        Map<String, Object> nestedMap = ImmutableMap.of(
                "nested-b", ImmutableMap.of("property-b2", 100)
        );

        // when
        Map<String, Object> props = MapUtil.flattenMap(nestedMap, DEFAULT_MERGE_FUNC);

        // then
        assertThat(props)
                .isNotEmpty()
                .hasSize(1)
                .containsEntry("nested-b.property-b2", "100");
    }

    @Test
    public void flattenMap_with2Levels_withDifferentMergeFunction() {
        Map<String, Object> nestedMap = ImmutableMap.of(
                "nested-b", ImmutableMap.of("property-b2", 100)
        );

        // when
        Map<String, Object> props = MapUtil.flattenMap(nestedMap, (k1, k2) -> k1 + "/" + k2);

        // then
        assertThat(props)
                .isNotEmpty()
                .hasSize(1)
                .containsEntry("nested-b/property-b2", "100");
    }

    @Test
    public void flattenMap_with3Levels() {
        Map<String, Object> nestedMap = ImmutableMap.of(
                "nested-c", ImmutableMap.of("subnested-c2", ImmutableMap.of("property-c", 100))
        );

        // when
        Map<String, Object> props = MapUtil.flattenMap(nestedMap, DEFAULT_MERGE_FUNC);

        // then
        assertThat(props)
                .isNotEmpty()
                .hasSize(1)
                .containsEntry("nested-c.subnested-c2.property-c", "100");
    }


    @Test
    public void flattenMap_withAllLevels() {
        Map<String, Object> nestedMap = ImmutableMap.of(
                "property-a", 100,
                "nested-b", ImmutableMap.of(
                        "property-b1", 200,
                        "property-b2", 300
                ),
                "nested-c", ImmutableMap.of("nested-c2", ImmutableMap.of("property-c", 400))
        );
        BinaryOperator<String> mergeFunc = (k1, k2) -> k1 + "." + k2;

        // when
        Map<String, Object> props = MapUtil.flattenMap(nestedMap, mergeFunc);

        // then
        assertThat(props)
                .isNotEmpty()
                .hasSize(4)
                .containsEntry("property-a", "100")
                .containsEntry("nested-b.property-b1", "200")
                .containsEntry("nested-b.property-b2", "300")
                .containsEntry("nested-c.nested-c2.property-c", "400");
    }

}