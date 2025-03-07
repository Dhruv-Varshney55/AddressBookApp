package com.example.AddressBookApp.repository;

import com.example.AddressBookApp.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    public EmployeeEntity findByEmail(String email);
}