package com.waiter.javawaiter.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<Phone, String> {
    @Override
    public void initialize(Phone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!value.startsWith("8") && value.length() != 11) {
            return false;
        }
        if (!value.startsWith("+7") && value.length() != 12) {
            return false;
        }
        if (value.length() >= 13 || value.length() < 11) {
            return false;
        }
        return true;
    }
}
