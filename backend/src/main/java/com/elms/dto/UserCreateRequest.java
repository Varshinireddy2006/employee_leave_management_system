package com.elms.dto;

import com.elms.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    
    @NotBlank(message = "Employee code is required")
    @Size(max = 20, message = "Employee code must be at most 20 characters")
    private String employeeCode;
    
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Size(max = 15, message = "Phone must be at most 15 characters")
    private String phone;
    
    @Size(max = 50, message = "Department must be at most 50 characters")
    private String department;
    
    @Size(max = 50, message = "Designation must be at most 50 characters")
    private String designation;
    
    @NotNull(message = "Role is required")
    private User.Role role;
    
    private Long managerId;
}
