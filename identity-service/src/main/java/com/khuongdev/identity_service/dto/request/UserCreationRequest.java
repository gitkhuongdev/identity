package com.khuongdev.identity_service.dto.request;

import com.khuongdev.identity_service.exception.ErrorCode;
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
    @Size(min = 3, max = 50, message = "USERNAME_INVALID")
     String username;
    // Thêm annotation Size để validate password
    @Size(min = 8, message = "INVALID_PASSWORD")
     String password;
     String firstName;
     String lastName;
     LocalDate dob;
}
