package com.elms.controller;

import com.elms.dto.ApiResponse;
import com.elms.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Report generation APIs")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/leave-summary")
    @Operation(summary = "Get leave summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getLeaveSummary() {
        Map<String, Object> summary = reportService.getLeaveSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @GetMapping("/department-summary")
    @Operation(summary = "Get department summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDepartmentSummary() {
        Map<String, Object> summary = reportService.getDepartmentSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @GetMapping("/monthly-report")
    @Operation(summary = "Get monthly report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMonthlyReport(
            @RequestParam int year,
            @RequestParam int month) {
        Map<String, Object> report = reportService.getMonthlyReport(year, month);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/export-csv")
    @Operation(summary = "Export leave requests to CSV")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportToCsv() {
        byte[] csvData = reportService.exportToCsv();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "leave_requests.csv");

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }
}
