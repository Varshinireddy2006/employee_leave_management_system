package com.elms.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    
    Map<String, Object> getLeaveSummary();
    
    Map<String, Object> getDepartmentSummary();
    
    Map<String, Object> getMonthlyReport(int year, int month);
    
    byte[] exportToCsv();
}
