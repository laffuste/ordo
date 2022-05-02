package com.laffuste.ordo.validation.application.in;

import com.laffuste.ordo.validation.domain.Order;

import java.util.List;

public interface OrderValidationUseCase {

    /**
     * Returns all errors found in the validation.
     * If errors are rare and speed of response is an issue, another method could fail-fast.
     */
    List<String> validate(Order order);

}
