package com.elms.controller;

import com.elms.dto.ApiResponse;
import com.elms.dto.ApplyLeaveRequest;
import com.elms.dto.ApproveRejectRequest;
import com.elms.dto.LeaveRequestDto;
import com.elms.repository.UserRepository;
import com.elms.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@Tag(name = "Leave Requests", description = "Leave request management APIs")
@CrossOrigin(origins = "*")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get all leave requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getAllLeaveRequests() {
        List<LeaveRequestDto> requests = leaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success(requests));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get leave request by ID")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> getLeaveRequestById(@PathVariable Long id) {
        LeaveRequestDto request = leaveRequestService.getLeaveRequestById(id);
        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @GetMapping("/my-requests")
    @Operation(summary = "Get my leave requests")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getMyLeaveRequests(Authentication authentication) {
        String email = authentication.getName();
        Long userId = getUserIdFromEmail(email);
        List<LeaveRequestDto> requests = leaveRequestService.getUserLeaveRequests(userId);
        return ResponseEntity.ok(ApiResponse.success(requests));
    }

    @GetMapping("/team-requests")
    @Operation(summary = "Get team leave requests")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getTeamLeaveRequests(Authentication authentication) {
        String email = authentication.getName();
        Long managerId = getUserIdFromEmail(email);
        List<LeaveRequestDto> requests = leaveRequestService.getTeamLeaveRequests(managerId);
        return ResponseEntity.ok(ApiResponse.success(requests));
    }

    @PostMapping("/apply")
    @Operation(summary = "Apply for leave")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> applyForLeave(
            Authentication authentication,
            @Valid @RequestBody ApplyLeaveRequest request) {
        String email = authentication.getName();
        Long userId = getUserIdFromEmail(email);
        LeaveRequestDto leaveRequest = leaveRequestService.applyForLeave(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Leave request submitted successfully", leaveRequest));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve leave request")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> approveLeave(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody ApproveRejectRequest request) {
        String email = authentication.getName();
        Long approverId = getUserIdFromEmail(email);
        LeaveRequestDto leaveRequest = leaveRequestService.approveLeave(id, approverId, request);
        return ResponseEntity.ok(ApiResponse.success("Leave request approved successfully", leaveRequest));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject leave request")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> rejectLeave(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody ApproveRejectRequest request) {
        String email = authentication.getName();
        Long approverId = getUserIdFromEmail(email);
        LeaveRequestDto leaveRequest = leaveRequestService.rejectLeave(id, approverId, request);
        return ResponseEntity.ok(ApiResponse.success("Leave request rejected successfully", leaveRequest));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel leave request")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> cancelLeave(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        Long userId = getUserIdFromEmail(email);
        LeaveRequestDto leaveRequest = leaveRequestService.cancelLeave(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Leave request cancelled successfully", leaveRequest));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get leave requests by status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getLeaveRequestsByStatus(@PathVariable String status) {
        List<LeaveRequestDto> requests = leaveRequestService.getLeaveRequestsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(requests));
    }

    private Long getUserIdFromEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}
