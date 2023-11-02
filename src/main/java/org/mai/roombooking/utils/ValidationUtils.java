package org.mai.roombooking.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import org.mai.roombooking.exceptions.DTOValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationUtils {


    private final Validator validator;

    @Autowired
    public ValidationUtils(Validator validator) {
        this.validator = validator;
    }


    public <T> void validationRequest(@NonNull T req) {

        Set<ConstraintViolation<T>> result = validator.validate(req);
        if (!result.isEmpty()) {
            String resultValidations = result.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((s1, s2) -> s1 + ". " + s2).orElse("");
            throw new DTOValidationException(resultValidations);

        }

    }
}