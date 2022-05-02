package com.laffuste.ordo.validation.application;

import com.laffuste.ordo.validation.OrdoValidationProperties;
import com.laffuste.ordo.validation.application.in.OrderValidationUseCase;
import com.laffuste.ordo.validation.application.validators.BaseValidator;
import com.laffuste.ordo.validation.application.validators.NotionalValidator;
import com.laffuste.ordo.validation.application.validators.QuantityValidator;
import com.laffuste.ordo.validation.application.validators.TypeValidator;
import com.laffuste.ordo.validation.domain.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
public class OrderValidationService implements OrderValidationUseCase {

    private final BaseValidator<Order> validatorChain;

    public OrderValidationService(OrdoValidationProperties properties) {
        // build validation chain
        this.validatorChain = new NotionalValidator(properties.getDefaultMarketOrderPrice(), properties.getNotionalValidatorMarketTypeThreshold(), properties.getNotionalValidatorLimitTypeThreshold())
                .add(new QuantityValidator(properties.getQuantityValidatorLimit()))
                .add(new TypeValidator());
    }

    @Override
    public List<String> validate(Order order) {
        if (order == null) {
            log.error("Submitted null order, skipping validation.");
            return emptyList();
        }
        log.info("Validating order id {}: {}}", order.getId(), order);
        List<String> errors = new ArrayList<>();
        validatorChain.validate(order, errors);
        if (!errors.isEmpty()) {
            log.warn("Errors were found in submitted order with id {}: {}", order.getId(), errors);
        }
        return errors;
    }
}
