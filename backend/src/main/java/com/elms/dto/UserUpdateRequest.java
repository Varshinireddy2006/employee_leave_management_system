package com.elms.dto;

import com.elms.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;
    
    @Size(max = 15, message = "Phone must be at most 15 characters")
    private String phone;
    
    @Size(max = 50, message = "Department must be at most 50 characters")
    private String department;
    
    @Size(max = 50, message = "Designation must be at most 50 characters")
    private String designation;
    
    private Long managerId;
}
