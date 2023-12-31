package org.mai.roombooking.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.mai.roombooking.exceptions.CustomValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {
    Validator validator;
    @Autowired
    ValidationService(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T value) {
        StringBuilder errors = new StringBuilder();
        Set<ConstraintViolation<T>> violations = validator.validate(value);
        for (var violation : violations) {
            errors.append(violation.getMessage());
        }

        if (!errors.isEmpty())
            throw new CustomValidationException(errors.toString());
    }
}
