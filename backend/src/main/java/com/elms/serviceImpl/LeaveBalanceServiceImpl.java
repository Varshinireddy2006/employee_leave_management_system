package com.elms.serviceImpl;

import com.elms.dto.LeaveBalanceDto;
import com.elms.entity.LeaveBalance;
import com.elms.entity.LeaveType;
import com.elms.entity.User;
import com.elms.repository.LeaveBalanceRepository;
import com.elms.repository.LeaveTypeRepository;
import com.elms.repository.UserRepository;
import com.elms.service.LeaveBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Override
    public List<LeaveBalanceDto> getAllLeaveBalances() {
        return leaveBalanceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveBalanceDto> getUserLeaveBalances(Long userId) {
        return leaveBalanceRepository.findAll().stream()
                .filter(lb -> lb.getUserId().equals(userId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveBalanceDto getUserLeaveBalance(Long userId, Long leaveTypeId) {
        LeaveBalance leaveBalance = leaveBalanceRepository.findByUserIdAndLeaveTypeId(userId, leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
        return convertToDto(leaveBalance);
    }

    @Override
    public LeaveBalanceDto createLeaveBalance(Long userId, Long leaveTypeId, Integer days) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        if (leaveBalanceRepository.findByUserIdAndLeaveTypeId(userId, leaveTypeId).isPresent()) {
            throw new RuntimeException("Leave balance already exists for this user and leave type");
        }

        LeaveBalance leaveBalance = new LeaveBalance();
        leaveBalance.setUserId(userId);
        leaveBalance.setLeaveTypeId(leaveTypeId);
        leaveBalance.setRemainingDays(days);

        LeaveBalance savedBalance = leaveBalanceRepository.save(leaveBalance);
        return convertToDto(savedBalance);
    }

    @Override
    public LeaveBalanceDto updateLeaveBalance(Long userId, Long leaveTypeId, Integer days) {
        LeaveBalance leaveBalance = leaveBalanceRepository.findByUserIdAndLeaveTypeId(userId, leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        leaveBalance.setRemainingDays(days);

        LeaveBalance updatedBalance = leaveBalanceRepository.save(leaveBalance);
        return convertToDto(updatedBalance);
    }

    @Override
    public void deleteLeaveBalance(Long userId, Long leaveTypeId) {
        leaveBalanceRepository.deleteByUserIdAndLeaveTypeId(userId, leaveTypeId);
    }

    @Override
    public void deductLeaveBalance(Long userId, Long leaveTypeId, Integer days) {
        LeaveBalance leaveBalance = leaveBalanceRepository.findByUserIdAndLeaveTypeId(userId, leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        if (leaveBalance.getRemainingDays() < days) {
            throw new RuntimeException("Insufficient leave balance");
        }

        leaveBalance.setRemainingDays(leaveBalance.getRemainingDays() - days);
        leaveBalanceRepository.save(leaveBalance);
    }

    private LeaveBalanceDto convertToDto(LeaveBalance leaveBalance) {
        LeaveBalanceDto dto = new LeaveBalanceDto();
        dto.setId(leaveBalance.getId());
        dto.setUserId(leaveBalance.getUserId());
        
        User user = userRepository.findById(leaveBalance.getUserId()).orElse(null);
        dto.setUserName(user != null ? user.getFullName() : null);
        
        dto.setLeaveTypeId(leaveBalance.getLeaveTypeId());
        
        LeaveType leaveType = leaveTypeRepository.findById(leaveBalance.getLeaveTypeId()).orElse(null);
        dto.setLeaveTypeName(leaveType != null ? leaveType.getLeaveName() : null);
        
        dto.setRemainingDays(leaveBalance.getRemainingDays());
        return dto;
    }
}
