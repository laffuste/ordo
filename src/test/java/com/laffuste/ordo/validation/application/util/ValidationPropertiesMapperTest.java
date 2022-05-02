package com.laffuste.ordo.validation.application.util;

import com.laffuste.ordo.properties.domain.TypedProperties;
import com.laffuste.ordo.validation.OrdoValidationProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class ValidationPropertiesMapperTest {

    private static final String VALIDATOR_NOTIONAL_LIMIT_ORDER_LIMIT = "validator.notional.limit-order.limit";
    private static final String VALIDATOR_NOTIONAL_MARKET_ORDER_LIMIT = "validator.notional.market-order.limit";
    private static final String VALIDATOR_QUANTITY_LIMIT = "validator.quantity.limit";
    private static final String DEFAULT_MARKET_ORDER_PRICE = "order.market.default-price";

    @Test
    public void toOrdoProperties() {
        // given
        TypedProperties typedProps = new TypedProperties(4);
        typedProps.put("validator.notional.limit-order.limit", 100);
        typedProps.put("validator.notional.market-order.limit", 200);
        typedProps.put("validator.quantity.limit", 300);
        typedProps.put("order.market.default-price", 400);

        // when
        OrdoValidationProperties props = ValidationPropertiesMapper.toOrdoProperties(typedProps);

        // then
        assertThat(props).isNotNull();
        assertThat(props.getNotionalValidatorLimitTypeThreshold()).isEqualTo(100);
        assertThat(props.getNotionalValidatorMarketTypeThreshold()).isEqualTo(200);
        assertThat(props.getQuantityValidatorLimit()).isEqualTo(300);
        assertThat(props.getDefaultMarketOrderPrice()).isEqualTo(400);
    }

    @Test
    public void toOrdoProperties_whenEmpty() {
        // given
        TypedProperties typedProps = new TypedProperties(4);

        // when
        Throwable t = catchThrowable(() -> ValidationPropertiesMapper.toOrdoProperties(typedProps));

        // then
        assertThat(t)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(" Property validator.notional.limit-order.limit is not a double: null");
    }

}