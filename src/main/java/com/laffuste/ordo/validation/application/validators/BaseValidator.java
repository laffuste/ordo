package com.laffuste.ordo.validation.application.validators;

import java.util.List;

/**
 * Implements chain of responsibility / filter chain pattern.
 */
public abstract class BaseValidator<T> {

    private BaseValidator<T> next;

    public BaseValidator<T> add(BaseValidator<T> next) {
        BaseValidator<T> pointer = this;
        while (pointer.next != null) {
            // skip to append to the last node. This traversal is wasteful but only done at startup for very few nodes.
            pointer = pointer.next;
        }
        pointer.next = next;
        return this;
    }

    protected void validateNext(T objToValidate, List<String> errors) {
        if (next == null) {
            return;
        }
        next.validate(objToValidate, errors);
    }

    /**
     * Main validation method.
     * Implementations must call validateNext() at some point.
     * Validation errors are added to errors arg.
     **/
    public abstract void validate(T objToValidate, List<String> errors);

}
