package com.khuongdev.identity_service.mapper;

import com.khuongdev.identity_service.dto.request.RoleRequest;
import com.khuongdev.identity_service.dto.respone.RoleResponse;
import com.khuongdev.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
