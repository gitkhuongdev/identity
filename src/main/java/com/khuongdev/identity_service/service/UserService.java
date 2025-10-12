package com.khuongdev.identity_service.service;

import com.khuongdev.identity_service.dto.request.UserCreationRequest;
import com.khuongdev.identity_service.dto.request.UserUpdateRequest;
import com.khuongdev.identity_service.dto.respone.UserResponse;
import com.khuongdev.identity_service.entity.User;
import com.khuongdev.identity_service.enums.Role;
import com.khuongdev.identity_service.exception.AppException;
import com.khuongdev.identity_service.exception.ErrorCode;
import com.khuongdev.identity_service.mapper.UserMapper;
import com.khuongdev.identity_service.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

// Đánh dấu đây là 1 service
// Spring sẽ tạo bean cho class này, bean có scope mặc định là singleton.
// Thường dùng để chứa logic nghiệp vụ (business logic)
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
// Đây là field injection: Spring sẽ inject instance của UserRepository vào trường này.
// Sau khi ứng dụng chạy, userRepository có thể dùng save, findById,
//    @Autowired
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    // Method tạo User
    public UserResponse createUser(@Valid UserCreationRequest request) {
//        User user = new User();
        // Đoạn này GlobalHandler exception sẽ xử lý để lấy message để trả về
        // Kết hợp với hàm existByUsername trong userRepo để JPA check username trong request có tồn tại hay chưa rồi trả về message
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Set role User
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

//        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get user");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id){
        log.info("In method get user by ID");
//        Phần message này chỉ xuất hiện khi thêm phần xử lý trong exception
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }
}
