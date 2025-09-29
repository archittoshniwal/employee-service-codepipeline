package com.codeShuttle.practice.TestingApplication.repositories;

import com.codeShuttle.practice.TestingApplication.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmail(String email);
}
