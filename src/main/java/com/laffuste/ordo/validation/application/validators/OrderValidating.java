package com.laffuste.ordo.validation.application.validators;

import com.laffuste.ordo.validation.domain.Order;

import java.util.Optional;

@FunctionalInterface
public interface OrderValidating {
    /**
     * Validates an order.
     */
    Optional<String> validate(Order order);

}
