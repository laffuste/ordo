package com.laffuste.ordo.validation.application.util;

import com.laffuste.ordo.properties.domain.TypedProperties;
import com.laffuste.ordo.validation.OrdoValidationProperties;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationPropertiesMapper {

    private static final String VALIDATOR_NOTIONAL_LIMIT_ORDER_LIMIT = "validator.notional.limit-order.limit";
    private static final String VALIDATOR_NOTIONAL_MARKET_ORDER_LIMIT = "validator.notional.market-order.limit";
    private static final String VALIDATOR_QUANTITY_LIMIT = "validator.quantity.limit";
    private static final String DEFAULT_MARKET_ORDER_PRICE = "order.market.default-price";

    /**
     * Mapping of properties to model
     */
    public static OrdoValidationProperties toOrdoProperties(TypedProperties properties) {
        double notionalLimitLimit = properties.getDouble(VALIDATOR_NOTIONAL_LIMIT_ORDER_LIMIT);
        double notionalMarketLimit = properties.getDouble(VALIDATOR_NOTIONAL_MARKET_ORDER_LIMIT);
        int quantityLimit = properties.getInt(VALIDATOR_QUANTITY_LIMIT);
        int defaultMarketOrderPrice = properties.getInt(DEFAULT_MARKET_ORDER_PRICE);
        return OrdoValidationProperties.builder()
                .notionalValidatorLimitTypeThreshold(notionalLimitLimit)
                .notionalValidatorMarketTypeThreshold(notionalMarketLimit)
                .quantityValidatorLimit(quantityLimit)
                .defaultMarketOrderPrice(defaultMarketOrderPrice)
                .build();
    }

}
