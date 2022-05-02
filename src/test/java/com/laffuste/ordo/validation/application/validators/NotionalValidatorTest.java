package com.laffuste.ordo.validation.application.validators;

import com.laffuste.ordo.validation.domain.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotionalValidatorTest {

    private final NotionalValidator notionalValidator = new NotionalValidator(200, 20_000_000, 10_000_000);

    @Test
    void validate_whenOrderNull() {
        // given
        Order order = null;

        // when
        List<String> errors = new ArrayList<>();
        notionalValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_whenOrderTypeMissing() {
        // given
        Order order = Order.builder()
                .quantity(1_000_000)
                .price(1_000_000)
                .build();

        // when
        List<String> errors = new ArrayList<>();
        notionalValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_whenOrderTypeWrong_expectError() {
        // given
        Order order = Order.builder()
                .type("trailing-stop")  // doesn't exist
                .quantity(1_000_000)
                .price(1_000_000)
                .build();

        // when
        List<String> errors = new ArrayList<>();
        notionalValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_withLimit_whenBelowThreshold() {
        // given
        Order order = Order.builder()
                .type("limit")
                .price(2.0)
                .quantity(4_999_999) // just below threshold
                .build();

        // when
        List<String> errors = new ArrayList<>();
        notionalValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_withLimit_whenAboveThreshold_expectError() {
        // given
        Order order = Order.builder()
                .type("LIMIT")
                .price(2.0)
                .quantity(5_000_000) // just on threshold
                .build();

        // when
        List<String> errors = new ArrayList<>();
        notionalValidator.validate(order, errors);

        // then
        assertThat(errors)
                .hasSize(1)
                .contains("Order's notional (10000000.00) is equal or greater than the risk threshold for limit orders (10000000.00)");
    }

    @Test
    void validate_withMarket_whenBelowThreshold() {
        // given
        Order order = Order.builder()
                .type("market")
                .quantity(99_999) // just below threshold
                .build();

        // when
        List<String> errors = new ArrayList<>();
        notionalValidator.validate(order, errors);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_withMarket_whenAboveThreshold_expectError() {
        // given
        Order order = Order.builder()
                .type("MARKET")
                .quantity(100_000) // just on threshold
                .build();

        // when
        List<String> errors = new ArrayList<>();
        notionalValidator.validate(order, errors);

        // then
        assertThat(errors)
                .hasSize(1)
                .contains("Order's notional (20000000.00, based on market's default price 200.00) is equal or greater than the risk threshold for market orders (20000000.00)");
    }

}