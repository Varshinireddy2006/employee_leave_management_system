package com.elms.service;

import com.elms.dto.LeaveTypeDto;
import com.elms.dto.LeaveTypeRequest;

import java.util.List;

public interface LeaveTypeService {
    
    List<LeaveTypeDto> getAllLeaveTypes();
    
    LeaveTypeDto getLeaveTypeById(Long id);
    
    LeaveTypeDto createLeaveType(LeaveTypeRequest request);
    
    LeaveTypeDto updateLeaveType(Long id, LeaveTypeRequest request);
    
    void deleteLeaveType(Long id);
}
