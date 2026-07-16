package com.elms.serviceImpl;

import com.elms.dto.UserCreateRequest;
import com.elms.dto.UserDto;
import com.elms.dto.UserUpdateRequest;
import com.elms.entity.User;
import com.elms.repository.UserRepository;
import com.elms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.elms.repository.LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private com.elms.repository.LeaveBalanceRepository leaveBalanceRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    @Override
    public UserDto createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new RuntimeException("Employee code already exists");
        }

        User user = new User();
        user.setEmployeeCode(request.getEmployeeCode());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setDepartment(request.getDepartment());
        user.setDesignation(request.getDesignation());
        user.setRole(request.getRole());
        user.setManagerId(request.getManagerId());

        User savedUser = userRepository.save(user);

        // Auto-initialize leave balances for employees
        if (savedUser.getRole() == User.Role.EMPLOYEE) {
            leaveTypeRepository.findAll().forEach(lt -> {
                com.elms.entity.LeaveBalance balance = new com.elms.entity.LeaveBalance();
                balance.setUserId(savedUser.getId());
                balance.setLeaveTypeId(lt.getId());
                balance.setRemainingDays(lt.getMaximumDays());
                leaveBalanceRepository.save(balance);
            });
        }

        return convertToDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setDepartment(request.getDepartment());
        user.setDesignation(request.getDesignation());
        user.setManagerId(request.getManagerId());

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getTeamMembers(Long managerId) {
        return userRepository.findByManagerId(managerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmployeeCode(user.getEmployeeCode());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setDepartment(user.getDepartment());
        dto.setDesignation(user.getDesignation());
        dto.setRole(user.getRole());
        dto.setManagerId(user.getManagerId());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
