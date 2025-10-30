package com.khuongdev.identity_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

// Best practice: Mỗi annotation chỉ nên xử lý cho 1 validation cụ thể

// ConstraintValidator cần implement 2 method đê có thể sử dụng
public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;

    @Override
    // Sẽ được khởi tạo khi dùng và lấy các giá trị của annotation đó
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if(Objects.isNull(value))
            return true;
        // Tính độ tuổi khi người dùng nhập vào
        long years = ChronoUnit.YEARS.between(value, LocalDate.now());

        return years >=min;
    }

    @Override
    // Hàm xử lý data có đúng với giá trị cần hay không
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
}
