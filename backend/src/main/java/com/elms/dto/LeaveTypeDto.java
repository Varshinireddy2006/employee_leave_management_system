package com.elms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTypeDto {
    
    private Long id;
    private String leaveName;
    private Integer maximumDays;
}
