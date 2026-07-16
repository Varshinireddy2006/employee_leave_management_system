package com.elms.dto;

import com.elms.entity.LeaveRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDto {
    
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private Long leaveTypeId;
    private String leaveTypeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfDays;
    private String reason;
    private LeaveRequest.LeaveStatus status;
    private LocalDateTime appliedDate;
    private Long approvedBy;
    private String approvedByName;
    private LocalDateTime approvedDate;
    private String managerComments;
}
