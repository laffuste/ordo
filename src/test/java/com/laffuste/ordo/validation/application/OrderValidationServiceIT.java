package com.laffuste.ordo.validation.application;

import com.laffuste.ordo.validation.application.properties.OrdoValidationProperties;
import com.laffuste.ordo.validation.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderValidationServiceIT {
    private OrderValidationService orderValidationService;

    @BeforeEach
    public void setup() {
        OrdoValidationProperties properties = OrdoValidationProperties.builder()
                .quantityValidatorLimit(10_000)
                .notionalValidatorMarketTypeThreshold(11_000)
                .notionalValidatorLimitTypeThreshold(12_000)
                .defaultMarketOrderPrice(1_000)
                .build();
        orderValidationService = new OrderValidationService(properties);
    }

    @Test
    public void validate() {
        // given
        Order order = Order.builder()
                .id("123")
                .type("LIMIT")
                .isBuy(true)
                .quantity(1_000)
                .price(1.1)
                .build();

        // when
        List<String> errors = orderValidationService.validate(order);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    public void validate_whenNullOrder_expectError() {
        // given
        Order order = null;

        // when
        List<String> errors = orderValidationService.validate(order);

        // then
        assertThat(errors).isEmpty();
    }

    @Test
    public void validate_whenInvalid_expectErrors() {
        // given
        Order order = Order.builder()
                .id("123")
                .type("MARKET")
                .isBuy(true)
                .quantity(1_000_000_000)
                .price(999999.0)
                .build();

        // when
        List<String> errors = orderValidationService.validate(order);

        // then
        assertThat(errors)
                .hasSize(2)
                .containsExactly(
                        "Order's notional (1000000000000.00, based on market's default price 1000.00) is equal or greater than the risk threshold for market orders (11000.00)",
                        "Order quantity (1000000000) is equals or greater than the risk threshold (10000)"
                );
    }

    @Test
    public void validate_whenWrongType_expectErrors() {
        // given
        Order order = Order.builder()
                .id("123")
                .type("what?")
                .isBuy(true)
                .quantity(1_000_000_000)
                .price(999999.0)
                .build();

        // when
        List<String> errors = orderValidationService.validate(order);

        // then
        assertThat(errors)
                .hasSize(2)
                .containsExactly(
                        "Order quantity (1000000000) is equals or greater than the risk threshold (10000)",
                        "Order type is invalid: what?"
                );
    }

}
