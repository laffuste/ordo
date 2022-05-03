package com.laffuste.ordo.validation.application.validators;

import com.google.common.collect.ImmutableSet;
import com.laffuste.ordo.validation.domain.Order;

import java.util.List;
import java.util.Set;

import static org.apache.logging.log4j.util.Strings.isBlank;

public class TypeValidator extends BaseValidator<Order> {
    private static final Set<String> validTypes = ImmutableSet.of(Order.TYPE_LIMIT, Order.TYPE_MARKET);

    @Override
    public void validate(Order order, List<String> errors) {
        if (order != null) {
            String type = order.getType();
            if (isBlank(type)) {
                errors.add("Order type cannot be empty");
                return;
            }

            if (!validTypes.contains(type)) {
                errors.add("Order type is invalid: " + type);
            }
        }
        validateNext(order, errors);
    }

}
