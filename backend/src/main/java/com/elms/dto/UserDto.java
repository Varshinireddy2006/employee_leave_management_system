package com.elms.dto;

import com.elms.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String designation;
    private User.Role role;
    private Long managerId;
    private LocalDateTime createdAt;
}
