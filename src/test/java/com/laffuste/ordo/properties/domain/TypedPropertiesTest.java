package com.laffuste.ordo.properties.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class TypedPropertiesTest {

//    private final TypedProperties properties = TypedProperties.ofProperties(new Properties());
    private final TypedProperties properties = new TypedProperties(2);

    @Test
    void getInt() {
        // given
        properties.put("my-int", 10);

        // when
        int i = properties.getInt("my-int");

        // then
        assertThat(i).isEqualTo(10);
    }

    @Test
    void getInt_whenNotInteger_expectError() {
        // given
        properties.put("not-an-int", "oops");

        // when
        Throwable t = catchThrowable(() -> properties.getInt("not-an-int"));

        // then
        assertThat(t)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Property not-an-int is not an integer: oops");
    }

    @Test
    void getDouble() {
        // given
        properties.put("my-double", 10.1);

        // when
        double d = properties.getDouble("my-double");

        // then
        assertThat(d).isEqualTo(10.1);
    }

    @Test
    void getDouble_whenNotDouble_expectError() {
        // given
        properties.put("not-a-double", "oops");

        // when
        Throwable t = catchThrowable(() -> properties.getDouble("not-a-double"));

        // then
        assertThat(t)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Property not-a-double is not a double: oops");
    }
}