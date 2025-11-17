package com.khuongdev.identity_service.service;

import com.khuongdev.identity_service.dto.request.UserCreationRequest;
import com.khuongdev.identity_service.dto.respone.UserResponse;
import com.khuongdev.identity_service.entity.User;
import com.khuongdev.identity_service.exception.AppException;
import com.khuongdev.identity_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest

public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData(){
        dob = LocalDate.of(2004, 1, 1);
        request = UserCreationRequest.builder()
                .username("john")
                .firstName("john")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("c5hdg577jj")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

        user = User.builder()
                .id("c5hdg577jj")
                .username("john")
                .firstName("john")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void create_validRequest_success(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // THEN
        Assertions.assertEquals("c5hdg577jj", response.getId());
        Assertions.assertEquals("john", response.getUsername());

    }

    @Test
    void create_userExisted_fail(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        assertEquals(1002, exception.getErrorCode().getCode());
        // THEN


    }

}
