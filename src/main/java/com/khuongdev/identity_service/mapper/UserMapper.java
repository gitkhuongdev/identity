package com.khuongdev.identity_service.mapper;

import com.khuongdev.identity_service.dto.request.UserCreationRequest;
import com.khuongdev.identity_service.dto.request.UserUpdateRequest;
import com.khuongdev.identity_service.dto.respone.UserResponse;
import com.khuongdev.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
//    @Mapping(source = "firstName", target = "lastName")
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
