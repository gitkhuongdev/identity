package com.khuongdev.identity_service.exception;

import com.khuongdev.identity_service.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

// Khai báo để Spring biết đây là file xử lý Exception
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Hằng dùng truy xuất attribute min từ ConstraintDescriptor khi cần map message
    private static final  String MIN_ATTRIBUTE = "min";
    // Xử lý các Exception chưa xử lý lỗi, tổng quát
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse();
        // Set err code và message cho phần exception chưa xử lý
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Xử lý các mã lỗi thuộc AppException
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception){
        // Lấy ErrorCode để setCode và getCode
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        // Trả về status code tương ứng và body
        return ResponseEntity
                        .status(errorCode
                        .getStatusCode())
                        .body(apiResponse);
    }

    // Xử lý Authorize khi người dùng không có quyền truy cập api
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    // Xử lý exception để lấy default message từ các phần validate
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        // Lấy default message từ các enum key của ErrorCode
        String enumKey = exception.getFieldError().getDefaultMessage();
        // Giá trị mặt định nếu không tìm được enum
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        try{
            // Dùng valueOf chuyển enumKey thành ErrorCode
            // Nếu enum ko hợp lệ thì valueOf sẽ ném IllegalArgumentException trong catch và fallback dùng INVALID_KEY
            errorCode = ErrorCode.valueOf(enumKey);
            // lấy ConstraintViolation từ ObjectError để truy ConstraintDescriptor và lấy attributes (như min, max,...)
            var constrainViolation = exception.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            // Lấy map attribute (key -> value), ví dụ {"min" -> 5, "message" -> "..."}.
            attributes = constrainViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());

        } catch (IllegalArgumentException ignored){

        }

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        // Nếu có attributes, gọi mapAttribute(...) để thay placeholder (ví dụ {min}) bằng giá trị thực.
        // Nếu không, trả message gốc từ ErrorCode
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                mapAttribute(errorCode.getMessage(), attributes)
                : errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // thay {min} trong message bằng giá trị thực lấy từ attributes map (attributes.get("min"))
    private String mapAttribute(String message, Map<String, Object> attributes){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
