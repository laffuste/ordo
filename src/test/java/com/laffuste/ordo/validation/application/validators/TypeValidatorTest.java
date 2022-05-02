package com.laffuste.ordo.validation.application.validators;

import com.laffuste.ordo.validation.domain.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TypeValidatorTest {

    private final TypeValidator typeValidator = new TypeValidator();

    @Test
    void validate_whenOrderNull() {
        // given
        Order order = null;

        // when
        List<String> errors = new ArrayList<>();
        typeValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_whenMarketOrder() {
        // given
        Order order = Order.builder()
                .type("MARKET")
                .build();

        // when
        List<String> errors = new ArrayList<>();
        typeValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_whenLimitOrder() {
        // given
        Order order = Order.builder()
                .type("LIMIT")
                .build();

        // when
        List<String> errors = new ArrayList<>();
        typeValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_whenInvalidType_expectError() {
        // given
        Order order = Order.builder()
                .type("trailing-stop") // invalid
                .build();

        // when
        List<String> errors = new ArrayList<>();
        typeValidator.validate(order, errors);

        // then
        assertThat(errors)
                .hasSize(1)
                .contains("Order type is invalid: trailing-stop");
    }

    @Test
    void validate_whenEmptyType_expectError() {
        // given
        Order order = Order.builder()
                .type("")
                .build();

        // when
        List<String> errors = new ArrayList<>();
        typeValidator.validate(order, errors);

        // then
        assertThat(errors)
                .hasSize(1)
                .contains("Order type cannot be empty");
    }
}