package com.khuongdev.identity_service.dto.request;

// Class này chứa các field để chuẩn hóa API
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// Dòng này để khi kết quả trả về của response có các dòng null thì sẽ không hiển thị dòng đó ra
@JsonInclude(JsonInclude.Include.NON_NULL)
// <T> là generic type, cho phép dùng các kiểu dữ liệu khác nhau cho từng response khác nhau
public class ApiResponse <T> {
    int code = 1000;
    String message;
    T result;
}
