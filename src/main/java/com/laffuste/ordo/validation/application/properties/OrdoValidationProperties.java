package com.laffuste.ordo.validation.application.properties;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Encapsulates properties.
 * Decouples from the property resolution so the exact property name doesn't leak through all the app.
 */
@Value
@RequiredArgsConstructor
@Builder
public class OrdoValidationProperties {
    double notionalValidatorMarketTypeThreshold;
    double notionalValidatorLimitTypeThreshold;
    double defaultMarketOrderPrice;
    int quantityValidatorLimit;
}
