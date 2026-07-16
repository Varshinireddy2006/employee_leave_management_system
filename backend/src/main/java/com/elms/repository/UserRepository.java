package com.elms.repository;

import com.elms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmployeeCode(String employeeCode);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmployeeCode(String employeeCode);
    
    List<User> findByRole(User.Role role);
    
    List<User> findByManagerId(Long managerId);
    
    List<User> findByDepartment(String department);
}
