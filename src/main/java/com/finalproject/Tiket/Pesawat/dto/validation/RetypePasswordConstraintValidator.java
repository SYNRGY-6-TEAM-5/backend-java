package com.finalproject.Tiket.Pesawat.dto.validation;

import com.finalproject.Tiket.Pesawat.dto.auth.request.RequestEditUser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class RetypePasswordConstraintValidator implements ConstraintValidator<ValidRetypePassword, Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        RequestEditUser user = (RequestEditUser) o;
        boolean valid = user.getNewPassword().equals(user.getRetypePassword());

        if (!valid) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("retypePassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }

    @Override
    public void initialize(ValidRetypePassword constraintAnnotation) {
    }
}
