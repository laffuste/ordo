package com.laffuste.ordo.validation.application.validators;

import com.laffuste.ordo.validation.domain.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuantityValidatorTest {

    private final QuantityValidator quantityValidator = new QuantityValidator(100);

    @Test
    void validate_whenOrderNull() {
        // given
        Order order = null;

        // when
        List<String> errors = new ArrayList<>();
        quantityValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_whenInvalidQuantity_expectError() {
        // given
        Order order = Order.builder()
                .quantity(0)
                .build();

        // when
        List<String> errors = new ArrayList<>();
        quantityValidator.validate(order, errors);

        // then
        assertThat(errors)
                .hasSize(1)
                .contains("Invalid order quantity: 0");
    }

    @Test
    void validate_whenBelowThreshold() {
        // given
        Order order = Order.builder()
                .quantity(99)
                .build();

        // when
        List<String> errors = new ArrayList<>();
        quantityValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_whenAboveThreshold_expectError() {
        // given
        Order order = Order.builder()
                .quantity(100)
                .build();

        // when
        List<String> errors = new ArrayList<>();
        quantityValidator.validate(order, errors);

        // then
        assertThat(errors)
                .hasSize(1)
                .contains("Order quantity (100) is equals or greater than the risk threshold (100)");
    }

}