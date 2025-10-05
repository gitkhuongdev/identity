package com.khuongdev.identity_service.service;

import com.khuongdev.identity_service.dto.request.UserCreationRequest;
import com.khuongdev.identity_service.dto.request.UserUpdateRequest;
import com.khuongdev.identity_service.dto.respone.UserResponse;
import com.khuongdev.identity_service.entity.User;
import com.khuongdev.identity_service.exception.AppException;
import com.khuongdev.identity_service.exception.ErrorCode;
import com.khuongdev.identity_service.mapper.UserMapper;
import com.khuongdev.identity_service.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Đánh dấu đây là 1 service
// Spring sẽ tạo bean cho class này, bean có scope mặc định là singleton.
// Thường dùng để chứa logic nghiệp vụ (business logic)
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
// Đây là field injection: Spring sẽ inject instance của UserRepository vào trường này.
// Sau khi ứng dụng chạy, userRepository có thể dùng save, findById,
//    @Autowired
    UserRepository userRepository;
    UserMapper userMapper;
    // Method tạo User
    public User createUser(@Valid UserCreationRequest request) {
//        User user = new User();
        // Đoạn này GlobalHandler exception sẽ xử lý để lấy message để trả về
        // Kết hợp với hàm existByUsername trong userRepo để JPA check username trong request có tồn tại hay chưa rồi trả về message
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());
        return  userRepository.save(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getUsers(){
        return  userRepository.findAll();
    }

    public UserResponse getUser(String id){
//        Phần message này chỉ xuất hiện khi thêm phần xử lý trong exception
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }
}
