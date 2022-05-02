package com.laffuste.ordo.validation.application.validators;

import com.laffuste.ordo.validation.OrdoValidationProperties;
import com.laffuste.ordo.validation.domain.Order;

import java.util.List;

import static java.lang.String.format;

public class NotionalValidator extends BaseValidator<Order> {

    private final double defaultMarketOrderPrice;
    private final double notionalMarketOrderThreshold;
    private final double notionalLimitOrderThreshold;

    public NotionalValidator(OrdoValidationProperties properties) {
        this.defaultMarketOrderPrice = properties.getDefaultMarketOrderPrice();
        this.notionalMarketOrderThreshold = properties.getNotionalValidatorMarketTypeThreshold();
        this.notionalLimitOrderThreshold = properties.getNotionalValidatorLimitTypeThreshold();
    }

    public NotionalValidator(double defaultMarketOrderPrice, double notionalMarketOrderThreshold, double notionalLimitOrderThreshold) {
        this.defaultMarketOrderPrice = defaultMarketOrderPrice;
        this.notionalMarketOrderThreshold = notionalMarketOrderThreshold;
        this.notionalLimitOrderThreshold = notionalLimitOrderThreshold;
    }

    @Override
    public void validate(Order order, List<String> errors) {
        if (order != null) {
            if (order.isLimitOrder()) {
                validateLimitOrder(order, errors);
            }
            if (order.isMarketOrder()) {
                validateMarketOrder(order, errors);
            }
        }
        validateNext(order, errors);
    }

    private void validateLimitOrder(Order order, List<String> errors) {
        double notional = order.calculateNotional();
        if (notional >= notionalLimitOrderThreshold) {
            String error = format("Order's notional (%.2f) is equal or greater than the risk threshold for limit orders (%.2f)", notional, notionalLimitOrderThreshold);
            errors.add(error);
        }
    }

    private void validateMarketOrder(Order order, List<String> errors) {
        double notional = order.getQuantity() * defaultMarketOrderPrice;
        if (notional >= notionalMarketOrderThreshold) {
            String error = format("Order's notional (%.2f, based on market's default price %.2f) is equal or greater than the risk threshold for market orders (%.2f)", notional, defaultMarketOrderPrice, notionalMarketOrderThreshold);
            errors.add(error);
        }
    }

}
