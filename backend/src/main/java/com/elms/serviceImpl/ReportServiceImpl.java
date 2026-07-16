package com.elms.serviceImpl;

import com.elms.entity.LeaveRequest;
import com.elms.entity.User;
import com.elms.repository.LeaveRequestRepository;
import com.elms.repository.UserRepository;
import com.elms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> getLeaveSummary() {
        List<LeaveRequest> allRequests = leaveRequestRepository.findAll();
        
        long pending = allRequests.stream().filter(r -> r.getStatus() == LeaveRequest.LeaveStatus.PENDING).count();
        long approved = allRequests.stream().filter(r -> r.getStatus() == LeaveRequest.LeaveStatus.APPROVED).count();
        long rejected = allRequests.stream().filter(r -> r.getStatus() == LeaveRequest.LeaveStatus.REJECTED).count();
        long cancelled = allRequests.stream().filter(r -> r.getStatus() == LeaveRequest.LeaveStatus.CANCELLED).count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRequests", allRequests.size());
        summary.put("pending", pending);
        summary.put("approved", approved);
        summary.put("rejected", rejected);
        summary.put("cancelled", cancelled);
        
        return summary;
    }

    @Override
    public Map<String, Object> getDepartmentSummary() {
        List<User> allUsers = userRepository.findAll();
        
        Map<String, Long> departmentCount = allUsers.stream()
                .filter(u -> u.getDepartment() != null)
                .collect(Collectors.groupingBy(User::getDepartment, Collectors.counting()));

        Map<String, Object> summary = new HashMap<>();
        summary.put("departments", departmentCount);
        summary.put("totalDepartments", departmentCount.size());
        
        return summary;
    }

    @Override
    public Map<String, Object> getMonthlyReport(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        List<LeaveRequest> monthlyRequests = leaveRequestRepository.findAll().stream()
                .filter(r -> !r.getAppliedDate().toLocalDate().isBefore(startOfMonth) 
                        && !r.getAppliedDate().toLocalDate().isAfter(endOfMonth))
                .collect(Collectors.toList());

        Map<String, Long> statusCounts = monthlyRequests.stream()
                .collect(Collectors.groupingBy(r -> r.getStatus().toString(), Collectors.counting()));

        Map<String, Object> report = new HashMap<>();
        report.put("month", month);
        report.put("year", year);
        report.put("totalRequests", monthlyRequests.size());
        report.put("statusBreakdown", statusCounts);
        
        return report;
    }

    @Override
    public byte[] exportToCsv() {
        List<LeaveRequest> allRequests = leaveRequestRepository.findAllByOrderByAppliedDateDesc();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        writer.println("ID,Employee,Email,Leave Type,Start Date,End Date,Days,Reason,Status,Applied Date");

        for (LeaveRequest request : allRequests) {
            User employee = userRepository.findById(request.getEmployeeId()).orElse(null);
            String employeeName = employee != null ? employee.getFullName() : "N/A";
            String employeeEmail = employee != null ? employee.getEmail() : "N/A";
            
            writer.printf("%d,%s,%s,%d,%s,%s,%d,%s,%s,%s%n",
                    request.getId(),
                    employeeName,
                    employeeEmail,
                    request.getLeaveTypeId(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getNumberOfDays(),
                    request.getReason() != null ? request.getReason().replace(",", ";") : "N/A",
                    request.getStatus(),
                    request.getAppliedDate()
            );
        }

        writer.flush();
        writer.close();

        return outputStream.toByteArray();
    }
}
