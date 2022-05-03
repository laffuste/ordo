package com.laffuste.ordo.validation.application.properties;

import com.laffuste.ordo.properties.domain.TypedProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class ValidationPropertiesMapperTest {

    @Test
    public void toOrdoProperties() {
        // given
        TypedProperties typedProps = new TypedProperties();
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
        TypedProperties typedProps = new TypedProperties();

        // when
        Throwable t = catchThrowable(() -> ValidationPropertiesMapper.toOrdoProperties(typedProps));

        // then
        assertThat(t)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Property validator.notional.limit-order.limit is not a double: null");
    }

}