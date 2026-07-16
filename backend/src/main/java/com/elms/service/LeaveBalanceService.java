package com.elms.service;

import com.elms.dto.LeaveBalanceDto;

import java.util.List;

public interface LeaveBalanceService {
    
    List<LeaveBalanceDto> getAllLeaveBalances();
    
    List<LeaveBalanceDto> getUserLeaveBalances(Long userId);
    
    LeaveBalanceDto getUserLeaveBalance(Long userId, Long leaveTypeId);
    
    LeaveBalanceDto createLeaveBalance(Long userId, Long leaveTypeId, Integer days);
    
    LeaveBalanceDto updateLeaveBalance(Long userId, Long leaveTypeId, Integer days);
    
    void deleteLeaveBalance(Long userId, Long leaveTypeId);
    
    void deductLeaveBalance(Long userId, Long leaveTypeId, Integer days);
}
