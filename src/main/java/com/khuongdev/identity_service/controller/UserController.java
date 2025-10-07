package com.khuongdev.identity_service.controller;

import com.khuongdev.identity_service.dto.request.ApiResponse;
import com.khuongdev.identity_service.dto.request.UserCreationRequest;
import com.khuongdev.identity_service.dto.request.UserUpdateRequest;
import com.khuongdev.identity_service.dto.respone.UserResponse;
import com.khuongdev.identity_service.entity.User;
import com.khuongdev.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// Khai báo để spring biết đây là controller xử lý endpoint
@RestController
// Định nghĩa /users để tránh lặp lại và không cần khai báo cho các mapping bên dưới
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    // @PostMapping("/users") khai báo từng mapping
    @PostMapping // Sau khi khai báo bên trên thì không cần dùng nữa

    // User createUser(@RequestBody @Valid UserCreationRequest request){
    // Thay vì chỉ trả về thông tin user thì sẽ trả về theo dạng chuẩn hóa
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    List<User> getUsers(){
        return  userService.getUsers();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId,@RequestBody UserUpdateRequest request){
        return userService.updateUser(userId,request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been delete";
    }
}
