package com.example.AddressBookApp.interfaces;

import java.util.*;
import com.example.AddressBookApp.dto.EmployeeDTO;
import com.example.AddressBookApp.dto.ResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeInterface {
    public EmployeeDTO get(Long id);
    public EmployeeDTO create(EmployeeDTO user);
    public String clear();
    public List<EmployeeDTO> getAll();
    public EmployeeDTO edit(EmployeeDTO user, Long id);
    public String delete(Long id);
    public ResponseDTO res(String message, String status);
}