package com.elms.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTypeRequest {
    
    @NotBlank(message = "Leave name is required")
    @Size(max = 50, message = "Leave name must be at most 50 characters")
    private String leaveName;
    
    @NotNull(message = "Maximum days is required")
    @Positive(message = "Maximum days must be positive")
    private Integer maximumDays;
}
