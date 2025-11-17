package com.khuongdev.identity_service.dto.request;

import com.khuongdev.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    // Thêm annotation Size để validate username
    @Size(min = 4, max = 50, message = "USERNAME_INVALID")
     String username;
    // Thêm annotation Size để validate password
    @Size(min = 8, message = "INVALID_PASSWORD")
     String password;
     String firstName;
     String lastName;

     // Khai báo và sử dụng annotation vừa custom để validate
     @DobConstraint(min = 16, message = "INVALID_DOB")
     LocalDate dob;
}
