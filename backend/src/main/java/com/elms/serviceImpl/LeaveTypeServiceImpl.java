package com.elms.serviceImpl;

import com.elms.dto.LeaveTypeDto;
import com.elms.dto.LeaveTypeRequest;
import com.elms.entity.LeaveType;
import com.elms.repository.LeaveTypeRepository;
import com.elms.service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Override
    public List<LeaveTypeDto> getAllLeaveTypes() {
        return leaveTypeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveTypeDto getLeaveTypeById(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));
        return convertToDto(leaveType);
    }

    @Override
    public LeaveTypeDto createLeaveType(LeaveTypeRequest request) {
        if (leaveTypeRepository.existsByLeaveName(request.getLeaveName())) {
            throw new RuntimeException("Leave type already exists");
        }

        LeaveType leaveType = new LeaveType();
        leaveType.setLeaveName(request.getLeaveName());
        leaveType.setMaximumDays(request.getMaximumDays());

        LeaveType savedLeaveType = leaveTypeRepository.save(leaveType);
        return convertToDto(savedLeaveType);
    }

    @Override
    public LeaveTypeDto updateLeaveType(Long id, LeaveTypeRequest request) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        leaveType.setLeaveName(request.getLeaveName());
        leaveType.setMaximumDays(request.getMaximumDays());

        LeaveType updatedLeaveType = leaveTypeRepository.save(leaveType);
        return convertToDto(updatedLeaveType);
    }

    @Override
    public void deleteLeaveType(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));
        leaveTypeRepository.delete(leaveType);
    }

    private LeaveTypeDto convertToDto(LeaveType leaveType) {
        LeaveTypeDto dto = new LeaveTypeDto();
        dto.setId(leaveType.getId());
        dto.setLeaveName(leaveType.getLeaveName());
        dto.setMaximumDays(leaveType.getMaximumDays());
        return dto;
    }
}
