package com.elms.service;

import com.elms.dto.UserCreateRequest;
import com.elms.dto.UserDto;
import com.elms.dto.UserUpdateRequest;
import com.elms.entity.User;

import java.util.List;

public interface UserService {
    
    List<UserDto> getAllUsers();
    
    UserDto getUserById(Long id);
    
    UserDto createUser(UserCreateRequest request);
    
    UserDto updateUser(Long id, UserUpdateRequest request);
    
    void deleteUser(Long id);
    
    List<UserDto> getUsersByRole(User.Role role);
    
    List<UserDto> getTeamMembers(Long managerId);
}
