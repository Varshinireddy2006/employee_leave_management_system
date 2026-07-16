package com.elms.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveRejectRequest {
    
    @Size(max = 500, message = "Comments must be at most 500 characters")
    private String comments;
}
