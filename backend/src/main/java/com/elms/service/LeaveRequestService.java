package com.elms.service;

import com.elms.dto.ApplyLeaveRequest;
import com.elms.dto.ApproveRejectRequest;
import com.elms.dto.LeaveRequestDto;

import java.util.List;

public interface LeaveRequestService {
    
    List<LeaveRequestDto> getAllLeaveRequests();
    
    LeaveRequestDto getLeaveRequestById(Long id);
    
    List<LeaveRequestDto> getUserLeaveRequests(Long userId);
    
    List<LeaveRequestDto> getTeamLeaveRequests(Long managerId);
    
    LeaveRequestDto applyForLeave(Long userId, ApplyLeaveRequest request);
    
    LeaveRequestDto approveLeave(Long id, Long approverId, ApproveRejectRequest request);
    
    LeaveRequestDto rejectLeave(Long id, Long approverId, ApproveRejectRequest request);
    
    LeaveRequestDto cancelLeave(Long id, Long userId);
    
    List<LeaveRequestDto> getLeaveRequestsByStatus(String status);
}
