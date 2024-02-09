package com.finalproject.Tiket.Pesawat.dto.validation;

import com.xendit.enums.BankCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BankCodeValidator implements ConstraintValidator<BankCodeValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        BankCode enumBankCode = BankCode.fromString(value);
        if (enumBankCode == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid bank code")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
