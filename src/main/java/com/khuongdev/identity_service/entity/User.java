package com.khuongdev.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
// Đây là một Class dùng để khai báo các cột trong DB User
// Khai báo để spring biết class User là 1 table thì dùng @Entity

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
// Đánh dấu trường id là primary key của entity — bắt buộc phải có một trường làm id.
    @Id
// Nói với persistence provider (Hibernate, EclipseLink...) rằng giá trị id sẽ tự động được sinh, và ở đây yêu cầu sinh theo UUID
    @GeneratedValue(strategy = GenerationType.UUID)
     String id;
     String username;
     String password;
     String firstName;
     String lastName;
     LocalDate dob;

    @ManyToMany
     Set<Role> roles; // Set khác List ở chỗ các item sẽ là duy nhất
}
