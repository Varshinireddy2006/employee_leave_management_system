package com.elms.controller;

import com.elms.dto.ApiResponse;
import com.elms.dto.LeaveBalanceDto;
import com.elms.service.LeaveBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-balances")
@Tag(name = "Leave Balances", description = "Leave balance management APIs")
@CrossOrigin(origins = "*")
public class LeaveBalanceController {

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @GetMapping
    @Operation(summary = "Get all leave balances")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LeaveBalanceDto>>> getAllLeaveBalances() {
        List<LeaveBalanceDto> balances = leaveBalanceService.getAllLeaveBalances();
        return ResponseEntity.ok(ApiResponse.success(balances));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get leave balances for a user")
    public ResponseEntity<ApiResponse<List<LeaveBalanceDto>>> getUserLeaveBalances(@PathVariable Long userId) {
        List<LeaveBalanceDto> balances = leaveBalanceService.getUserLeaveBalances(userId);
        return ResponseEntity.ok(ApiResponse.success(balances));
    }

    @GetMapping("/user/{userId}/leave-type/{leaveTypeId}")
    @Operation(summary = "Get specific leave balance for user")
    public ResponseEntity<ApiResponse<LeaveBalanceDto>> getUserLeaveBalance(
            @PathVariable Long userId,
            @PathVariable Long leaveTypeId) {
        LeaveBalanceDto balance = leaveBalanceService.getUserLeaveBalance(userId, leaveTypeId);
        return ResponseEntity.ok(ApiResponse.success(balance));
    }

    @PostMapping
    @Operation(summary = "Create leave balance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LeaveBalanceDto>> createLeaveBalance(
            @RequestParam Long userId,
            @RequestParam Long leaveTypeId,
            @RequestParam Integer days) {
        LeaveBalanceDto balance = leaveBalanceService.createLeaveBalance(userId, leaveTypeId, days);
        return ResponseEntity.ok(ApiResponse.success("Leave balance created successfully", balance));
    }

    @PutMapping("/user/{userId}/leave-type/{leaveTypeId}")
    @Operation(summary = "Update leave balance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LeaveBalanceDto>> updateLeaveBalance(
            @PathVariable Long userId,
            @PathVariable Long leaveTypeId,
            @RequestParam Integer days) {
        LeaveBalanceDto balance = leaveBalanceService.updateLeaveBalance(userId, leaveTypeId, days);
        return ResponseEntity.ok(ApiResponse.success("Leave balance updated successfully", balance));
    }

    @DeleteMapping("/user/{userId}/leave-type/{leaveTypeId}")
    @Operation(summary = "Delete leave balance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteLeaveBalance(
            @PathVariable Long userId,
            @PathVariable Long leaveTypeId) {
        leaveBalanceService.deleteLeaveBalance(userId, leaveTypeId);
        return ResponseEntity.ok(ApiResponse.success("Leave balance deleted successfully", null));
    }
}
