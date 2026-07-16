package com.elms.controller;

import com.elms.dto.ApiResponse;
import com.elms.dto.LeaveTypeDto;
import com.elms.dto.LeaveTypeRequest;
import com.elms.service.LeaveTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-types")
@Tag(name = "Leave Types", description = "Leave type management APIs")
@CrossOrigin(origins = "*")
public class LeaveTypeController {

    @Autowired
    private LeaveTypeService leaveTypeService;

    @GetMapping
    @Operation(summary = "Get all leave types")
    public ResponseEntity<ApiResponse<List<LeaveTypeDto>>> getAllLeaveTypes() {
        List<LeaveTypeDto> leaveTypes = leaveTypeService.getAllLeaveTypes();
        return ResponseEntity.ok(ApiResponse.success(leaveTypes));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get leave type by ID")
    public ResponseEntity<ApiResponse<LeaveTypeDto>> getLeaveTypeById(@PathVariable Long id) {
        LeaveTypeDto leaveType = leaveTypeService.getLeaveTypeById(id);
        return ResponseEntity.ok(ApiResponse.success(leaveType));
    }

    @PostMapping
    @Operation(summary = "Create new leave type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LeaveTypeDto>> createLeaveType(@Valid @RequestBody LeaveTypeRequest request) {
        LeaveTypeDto leaveType = leaveTypeService.createLeaveType(request);
        return ResponseEntity.ok(ApiResponse.success("Leave type created successfully", leaveType));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update leave type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LeaveTypeDto>> updateLeaveType(
            @PathVariable Long id,
            @Valid @RequestBody LeaveTypeRequest request) {
        LeaveTypeDto leaveType = leaveTypeService.updateLeaveType(id, request);
        return ResponseEntity.ok(ApiResponse.success("Leave type updated successfully", leaveType));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete leave type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteLeaveType(@PathVariable Long id) {
        leaveTypeService.deleteLeaveType(id);
        return ResponseEntity.ok(ApiResponse.success("Leave type deleted successfully", null));
    }
}
