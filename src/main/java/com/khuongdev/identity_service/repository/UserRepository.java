package com.khuongdev.identity_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khuongdev.identity_service.entity.User;

// Đánh dấu interface này là một Spring bean kiểu repository
@Repository
// extends JpaRepository<User, String>: chỉ ra rằng repository quản lý entity User,
// và kiểu dữ liệu của primary key (@Id) trong User là String
public interface UserRepository extends JpaRepository<User, String> {
    // Khi khai báo đoạn code này thì Spring JPA sẽ generate 1 câu query
    // Để kiểm tra có tồn tại username trong field username của DB hay chưa khi truyền vào từ endpoint
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
