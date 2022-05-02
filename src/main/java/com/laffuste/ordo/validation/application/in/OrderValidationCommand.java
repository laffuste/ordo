package com.laffuste.ordo.validation.application.in;

import com.laffuste.ordo.validation.domain.Order;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderValidationCommand {
    Order order;
}
