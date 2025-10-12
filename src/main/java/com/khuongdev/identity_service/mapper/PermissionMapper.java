package com.khuongdev.identity_service.mapper;

import com.khuongdev.identity_service.dto.request.PermissionRequest;
import com.khuongdev.identity_service.dto.respone.PermissionResponse;
import com.khuongdev.identity_service.entity.Permission;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
