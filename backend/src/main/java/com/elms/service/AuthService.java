package com.elms.service;

import com.elms.dto.*;

public interface AuthService {
    
    LoginResponse login(LoginRequest loginRequest);
    
    UserDto getProfile(String email);
    
    void changePassword(String email, ChangePasswordRequest request);
}
