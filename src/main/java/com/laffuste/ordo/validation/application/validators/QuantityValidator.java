package com.laffuste.ordo.validation.application.validators;

import com.laffuste.ordo.validation.domain.Order;

import java.util.List;

import static java.lang.String.format;

public class QuantityValidator extends BaseValidator<Order> {

    private final long quantityValidatorThreshold;

    public QuantityValidator(long quantityValidatorThreshold) {
        this.quantityValidatorThreshold = quantityValidatorThreshold;
    }

    @Override
    public void validate(Order order, List<String> errors) {
        if (order != null) {
            int quantity = order.getQuantity();
            if (order.getQuantity() <= 0) {
                errors.add("Invalid order quantity: " + quantity);
            }
            if (order.getQuantity() >= quantityValidatorThreshold) {
                errors.add(format("Order quantity (%s) is equals or greater than the risk threshold (%s)", quantity, quantityValidatorThreshold));
            }
        }
        validateNext(order, errors);
    }
}
