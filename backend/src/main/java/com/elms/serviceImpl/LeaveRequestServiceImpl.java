package com.elms.serviceImpl;

import com.elms.dto.ApplyLeaveRequest;
import com.elms.dto.ApproveRejectRequest;
import com.elms.dto.LeaveRequestDto;
import com.elms.entity.LeaveBalance;
import com.elms.entity.LeaveRequest;
import com.elms.entity.LeaveType;
import com.elms.entity.User;
import com.elms.repository.LeaveBalanceRepository;
import com.elms.repository.LeaveRequestRepository;
import com.elms.repository.LeaveTypeRepository;
import com.elms.repository.UserRepository;
import com.elms.service.LeaveBalanceService;
import com.elms.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @Override
    public List<LeaveRequestDto> getAllLeaveRequests() {
        return leaveRequestRepository.findAllByOrderByAppliedDateDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveRequestDto getLeaveRequestById(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        return convertToDto(leaveRequest);
    }

    @Override
    public List<LeaveRequestDto> getUserLeaveRequests(Long userId) {
        return leaveRequestRepository.findByEmployeeIdOrderByAppliedDateDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDto> getTeamLeaveRequests(Long managerId) {
        List<User> teamMembers = userRepository.findByManagerId(managerId);
        return teamMembers.stream()
                .flatMap(member -> leaveRequestRepository.findByEmployeeIdOrderByAppliedDateDesc(member.getId()).stream())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveRequestDto applyForLeave(Long userId, ApplyLeaveRequest request) {
        User employee = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        int numberOfDays = calculateWorkingDays(request.getStartDate(), request.getEndDate());

        LeaveBalance leaveBalance = leaveBalanceRepository.findByUserIdAndLeaveTypeId(userId, request.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave balance not found for this leave type"));

        if (leaveBalance.getRemainingDays() < numberOfDays) {
            throw new RuntimeException("Insufficient leave balance");
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(userId);
        leaveRequest.setLeaveTypeId(request.getLeaveTypeId());
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setNumberOfDays(numberOfDays);
        leaveRequest.setReason(request.getReason());
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.PENDING);

        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDto(savedRequest);
    }

    @Override
    public LeaveRequestDto approveLeave(Long id, Long approverId, ApproveRejectRequest request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be approved");
        }

        leaveRequest.setStatus(LeaveRequest.LeaveStatus.APPROVED);
        leaveRequest.setApprovedBy(approverId);
        leaveRequest.setApprovedDate(java.time.LocalDateTime.now());
        leaveRequest.setManagerComments(request.getComments());

        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);

        leaveBalanceService.deductLeaveBalance(leaveRequest.getEmployeeId(), 
                leaveRequest.getLeaveTypeId(), leaveRequest.getNumberOfDays());

        return convertToDto(updatedRequest);
    }

    @Override
    public LeaveRequestDto rejectLeave(Long id, Long approverId, ApproveRejectRequest request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be rejected");
        }

        leaveRequest.setStatus(LeaveRequest.LeaveStatus.REJECTED);
        leaveRequest.setApprovedBy(approverId);
        leaveRequest.setApprovedDate(java.time.LocalDateTime.now());
        leaveRequest.setManagerComments(request.getComments());

        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDto(updatedRequest);
    }

    @Override
    public LeaveRequestDto cancelLeave(Long id, Long userId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (!leaveRequest.getEmployeeId().equals(userId)) {
            throw new RuntimeException("You can only cancel your own leave requests");
        }

        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be cancelled");
        }

        leaveRequest.setStatus(LeaveRequest.LeaveStatus.CANCELLED);

        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDto(updatedRequest);
    }

    @Override
    public List<LeaveRequestDto> getLeaveRequestsByStatus(String status) {
        LeaveRequest.LeaveStatus leaveStatus = LeaveRequest.LeaveStatus.valueOf(status.toUpperCase());
        return leaveRequestRepository.findByStatus(leaveStatus).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        int workingDays = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY && current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            current = current.plusDays(1);
        }

        return workingDays;
    }

    private LeaveRequestDto convertToDto(LeaveRequest leaveRequest) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(leaveRequest.getId());
        dto.setEmployeeId(leaveRequest.getEmployeeId());
        
        User employee = userRepository.findById(leaveRequest.getEmployeeId()).orElse(null);
        dto.setEmployeeName(employee != null ? employee.getFullName() : null);
        dto.setEmployeeEmail(employee != null ? employee.getEmail() : null);
        
        dto.setLeaveTypeId(leaveRequest.getLeaveTypeId());
        
        LeaveType leaveType = leaveTypeRepository.findById(leaveRequest.getLeaveTypeId()).orElse(null);
        dto.setLeaveTypeName(leaveType != null ? leaveType.getLeaveName() : null);
        
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setNumberOfDays(leaveRequest.getNumberOfDays());
        dto.setReason(leaveRequest.getReason());
        dto.setStatus(leaveRequest.getStatus());
        dto.setAppliedDate(leaveRequest.getAppliedDate());
        dto.setApprovedBy(leaveRequest.getApprovedBy());
        
        if (leaveRequest.getApprovedBy() != null) {
            User approver = userRepository.findById(leaveRequest.getApprovedBy()).orElse(null);
            dto.setApprovedByName(approver != null ? approver.getFullName() : null);
        }
        
        dto.setApprovedDate(leaveRequest.getApprovedDate());
        dto.setManagerComments(leaveRequest.getManagerComments());
        
        return dto;
    }
}
