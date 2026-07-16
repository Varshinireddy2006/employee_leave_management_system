package com.elms.config;

import com.elms.entity.LeaveBalance;
import com.elms.entity.LeaveRequest;
import com.elms.entity.LeaveType;
import com.elms.entity.User;
import com.elms.repository.LeaveBalanceRepository;
import com.elms.repository.LeaveRequestRepository;
import com.elms.repository.LeaveTypeRepository;
import com.elms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            initializeUsers();
            initializeLeaveTypes();
            initializeLeaveBalances();
            initializeSampleLeaveRequests();
        }
    }

    private void initializeUsers() {
        // Admin
        User admin = new User();
        admin.setEmployeeCode("ADM001");
        admin.setFullName("Admin User");
        admin.setEmail("admin@elms.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setPhone("9876543210");
        admin.setDepartment("Administration");
        admin.setDesignation("System Administrator");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        // Managers
        User manager1 = new User();
        manager1.setEmployeeCode("MGR001");
        manager1.setFullName("John Manager");
        manager1.setEmail("manager1@elms.com");
        manager1.setPassword(passwordEncoder.encode("manager123"));
        manager1.setPhone("9876543211");
        manager1.setDepartment("Engineering");
        manager1.setDesignation("Engineering Manager");
        manager1.setRole(User.Role.MANAGER);
        userRepository.save(manager1);

        User manager2 = new User();
        manager2.setEmployeeCode("MGR002");
        manager2.setFullName("Sarah Manager");
        manager2.setEmail("manager2@elms.com");
        manager2.setPassword(passwordEncoder.encode("manager123"));
        manager2.setPhone("9876543212");
        manager2.setDepartment("Marketing");
        manager2.setDesignation("Marketing Manager");
        manager2.setRole(User.Role.MANAGER);
        userRepository.save(manager2);

        // Employees under Manager 1
        User emp1 = new User();
        emp1.setEmployeeCode("EMP001");
        emp1.setFullName("Alice Developer");
        emp1.setEmail("alice@elms.com");
        emp1.setPassword(passwordEncoder.encode("employee123"));
        emp1.setPhone("9876543213");
        emp1.setDepartment("Engineering");
        emp1.setDesignation("Software Developer");
        emp1.setRole(User.Role.EMPLOYEE);
        emp1.setManagerId(manager1.getId());
        userRepository.save(emp1);

        User emp2 = new User();
        emp2.setEmployeeCode("EMP002");
        emp2.setFullName("Bob Developer");
        emp2.setEmail("bob@elms.com");
        emp2.setPassword(passwordEncoder.encode("employee123"));
        emp2.setPhone("9876543214");
        emp2.setDepartment("Engineering");
        emp2.setDesignation("Software Developer");
        emp2.setRole(User.Role.EMPLOYEE);
        emp2.setManagerId(manager1.getId());
        userRepository.save(emp2);

        User emp3 = new User();
        emp3.setEmployeeCode("EMP003");
        emp3.setFullName("Charlie Developer");
        emp3.setEmail("charlie@elms.com");
        emp3.setPassword(passwordEncoder.encode("employee123"));
        emp3.setPhone("9876543215");
        emp3.setDepartment("Engineering");
        emp3.setDesignation("Senior Developer");
        emp3.setRole(User.Role.EMPLOYEE);
        emp3.setManagerId(manager1.getId());
        userRepository.save(emp3);

        // Employees under Manager 2
        User emp4 = new User();
        emp4.setEmployeeCode("EMP004");
        emp4.setFullName("Diana Marketing");
        emp4.setEmail("diana@elms.com");
        emp4.setPassword(passwordEncoder.encode("employee123"));
        emp4.setPhone("9876543216");
        emp4.setDepartment("Marketing");
        emp4.setDesignation("Marketing Specialist");
        emp4.setRole(User.Role.EMPLOYEE);
        emp4.setManagerId(manager2.getId());
        userRepository.save(emp4);

        User emp5 = new User();
        emp5.setEmployeeCode("EMP005");
        emp5.setFullName("Eve Marketing");
        emp5.setEmail("eve@elms.com");
        emp5.setPassword(passwordEncoder.encode("employee123"));
        emp5.setPhone("9876543217");
        emp5.setDepartment("Marketing");
        emp5.setDesignation("Content Writer");
        emp5.setRole(User.Role.EMPLOYEE);
        emp5.setManagerId(manager2.getId());
        userRepository.save(emp5);

        // More employees
        User emp6 = new User();
        emp6.setEmployeeCode("EMP006");
        emp6.setFullName("Frank HR");
        emp6.setEmail("frank@elms.com");
        emp6.setPassword(passwordEncoder.encode("employee123"));
        emp6.setPhone("9876543218");
        emp6.setDepartment("Human Resources");
        emp6.setDesignation("HR Specialist");
        emp6.setRole(User.Role.EMPLOYEE);
        emp6.setManagerId(manager1.getId());
        userRepository.save(emp6);

        User emp7 = new User();
        emp7.setEmployeeCode("EMP007");
        emp7.setFullName("Grace Finance");
        emp7.setEmail("grace@elms.com");
        emp7.setPassword(passwordEncoder.encode("employee123"));
        emp7.setPhone("9876543219");
        emp7.setDepartment("Finance");
        emp7.setDesignation("Accountant");
        emp7.setRole(User.Role.EMPLOYEE);
        emp7.setManagerId(manager2.getId());
        userRepository.save(emp7);

        User emp8 = new User();
        emp8.setEmployeeCode("EMP008");
        emp8.setFullName("Henry Sales");
        emp8.setEmail("henry@elms.com");
        emp8.setPassword(passwordEncoder.encode("employee123"));
        emp8.setPhone("9876543220");
        emp8.setDepartment("Sales");
        emp8.setDesignation("Sales Executive");
        emp8.setRole(User.Role.EMPLOYEE);
        emp8.setManagerId(manager1.getId());
        userRepository.save(emp8);

        User emp9 = new User();
        emp9.setEmployeeCode("EMP009");
        emp9.setFullName("Ivy Support");
        emp9.setEmail("ivy@elms.com");
        emp9.setPassword(passwordEncoder.encode("employee123"));
        emp9.setPhone("9876543221");
        emp9.setDepartment("Customer Support");
        emp9.setDesignation("Support Specialist");
        emp9.setRole(User.Role.EMPLOYEE);
        emp9.setManagerId(manager2.getId());
        userRepository.save(emp9);

        User emp10 = new User();
        emp10.setEmployeeCode("EMP010");
        emp10.setFullName("Jack QA");
        emp10.setEmail("jack@elms.com");
        emp10.setPassword(passwordEncoder.encode("employee123"));
        emp10.setPhone("9876543222");
        emp10.setDepartment("Engineering");
        emp10.setDesignation("QA Engineer");
        emp10.setRole(User.Role.EMPLOYEE);
        emp10.setManagerId(manager1.getId());
        userRepository.save(emp10);
    }

    private void initializeLeaveTypes() {
        LeaveType sickLeave = new LeaveType();
        sickLeave.setLeaveName("Sick Leave");
        sickLeave.setMaximumDays(10);
        leaveTypeRepository.save(sickLeave);

        LeaveType casualLeave = new LeaveType();
        casualLeave.setLeaveName("Casual Leave");
        casualLeave.setMaximumDays(12);
        leaveTypeRepository.save(casualLeave);

        LeaveType earnedLeave = new LeaveType();
        earnedLeave.setLeaveName("Earned Leave");
        earnedLeave.setMaximumDays(15);
        leaveTypeRepository.save(earnedLeave);
    }

    private void initializeLeaveBalances() {
        Long sickLeaveId = leaveTypeRepository.findByLeaveName("Sick Leave").get().getId();
        Long casualLeaveId = leaveTypeRepository.findByLeaveName("Casual Leave").get().getId();
        Long earnedLeaveId = leaveTypeRepository.findByLeaveName("Earned Leave").get().getId();

        for (User user : userRepository.findAll()) {
            if (user.getRole() == User.Role.EMPLOYEE) {
                LeaveBalance sickBalance = new LeaveBalance();
                sickBalance.setUserId(user.getId());
                sickBalance.setLeaveTypeId(sickLeaveId);
                sickBalance.setRemainingDays(10);
                leaveBalanceRepository.save(sickBalance);

                LeaveBalance casualBalance = new LeaveBalance();
                casualBalance.setUserId(user.getId());
                casualBalance.setLeaveTypeId(casualLeaveId);
                casualBalance.setRemainingDays(12);
                leaveBalanceRepository.save(casualBalance);

                LeaveBalance earnedBalance = new LeaveBalance();
                earnedBalance.setUserId(user.getId());
                earnedBalance.setLeaveTypeId(earnedLeaveId);
                earnedBalance.setRemainingDays(15);
                leaveBalanceRepository.save(earnedBalance);
            }
        }
    }

    private void initializeSampleLeaveRequests() {
        Long sickLeaveId = leaveTypeRepository.findByLeaveName("Sick Leave").get().getId();
        Long casualLeaveId = leaveTypeRepository.findByLeaveName("Casual Leave").get().getId();
        
        User alice = userRepository.findByEmail("alice@elms.com").get();
        User bob = userRepository.findByEmail("bob@elms.com").get();
        User manager1 = userRepository.findByEmail("manager1@elms.com").get();

        // Sample approved leave
        LeaveRequest approvedRequest = new LeaveRequest();
        approvedRequest.setEmployeeId(alice.getId());
        approvedRequest.setLeaveTypeId(casualLeaveId);
        approvedRequest.setStartDate(LocalDate.now().minusDays(10));
        approvedRequest.setEndDate(LocalDate.now().minusDays(8));
        approvedRequest.setNumberOfDays(3);
        approvedRequest.setReason("Personal work");
        approvedRequest.setStatus(LeaveRequest.LeaveStatus.APPROVED);
        approvedRequest.setAppliedDate(LocalDateTime.now().minusDays(15));
        approvedRequest.setApprovedBy(manager1.getId());
        approvedRequest.setApprovedDate(LocalDateTime.now().minusDays(14));
        approvedRequest.setManagerComments("Approved");
        leaveRequestRepository.save(approvedRequest);

        // Sample pending leave
        LeaveRequest pendingRequest = new LeaveRequest();
        pendingRequest.setEmployeeId(bob.getId());
        pendingRequest.setLeaveTypeId(sickLeaveId);
        pendingRequest.setStartDate(LocalDate.now().plusDays(5));
        pendingRequest.setEndDate(LocalDate.now().plusDays(6));
        pendingRequest.setNumberOfDays(2);
        pendingRequest.setReason("Not feeling well");
        pendingRequest.setStatus(LeaveRequest.LeaveStatus.PENDING);
        pendingRequest.setAppliedDate(LocalDateTime.now());
        leaveRequestRepository.save(pendingRequest);

        // Sample rejected leave
        LeaveRequest rejectedRequest = new LeaveRequest();
        rejectedRequest.setEmployeeId(alice.getId());
        rejectedRequest.setLeaveTypeId(casualLeaveId);
        rejectedRequest.setStartDate(LocalDate.now().minusDays(20));
        rejectedRequest.setEndDate(LocalDate.now().minusDays(18));
        rejectedRequest.setNumberOfDays(3);
        rejectedRequest.setReason("Vacation");
        rejectedRequest.setStatus(LeaveRequest.LeaveStatus.REJECTED);
        rejectedRequest.setAppliedDate(LocalDateTime.now().minusDays(25));
        rejectedRequest.setApprovedBy(manager1.getId());
        rejectedRequest.setApprovedDate(LocalDateTime.now().minusDays(24));
        rejectedRequest.setManagerComments("Insufficient leave balance");
        leaveRequestRepository.save(rejectedRequest);
    }
}
