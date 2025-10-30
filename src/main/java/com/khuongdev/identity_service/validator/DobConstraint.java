package com.khuongdev.identity_service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// Target là phạm vi sử dụng (nằm trong biến, method,...)
@Target({ElementType.FIELD})
// Annotation sẽ xử lý lúc nào
@Retention(RetentionPolicy.RUNTIME)
// Constraint dùng khai báo class sẽ chịu trách nhiệm cho annotation
@Constraint(validatedBy = {DobValidator.class})
// Khai báo annotation dùng @interface
public @interface DobConstraint {
    String message() default "Invalid date of birth";
    // Khai báo giá trị cần validate
    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
