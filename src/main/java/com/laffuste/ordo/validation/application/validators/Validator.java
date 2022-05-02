package com.laffuste.ordo.validation.application.validators;

import java.util.List;

@FunctionalInterface
public interface Validator<T> {

    void validate(T objToValidate, List<String> errors);

}
