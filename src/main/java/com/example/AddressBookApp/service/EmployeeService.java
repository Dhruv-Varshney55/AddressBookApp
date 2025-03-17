package com.example.AddressBookApp.service;

import com.example.AddressBookApp.dto.EmployeeDTO;
import com.example.AddressBookApp.dto.ResponseDTO;
import com.example.AddressBookApp.entity.EmployeeEntity;
import com.example.AddressBookApp.interfaces.EmployeeInterface;
import com.example.AddressBookApp.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService implements EmployeeInterface {

    ObjectMapper obj = new ObjectMapper();

    @Autowired
    EmployeeRepository empRepository;

    public ResponseDTO res(String message, String status){
        return new ResponseDTO(message, status);
    }

    public EmployeeDTO get(Long id) throws Exception{
        EmployeeEntity foundEmp = empRepository.findById(id).orElseThrow(()-> {
            log.error("Cannot find employee with id {}", id);
            return new RuntimeException("Can't find employee with this id");
        });
        EmployeeDTO edto = new EmployeeDTO(foundEmp.getName(), foundEmp.getEmail(), foundEmp.getId());
        log.info("Employee DTO send for id: {} is : {}", id, obj.writeValueAsString(edto));
        return edto;
    }

    public EmployeeDTO create(EmployeeDTO user) throws Exception{
        EmployeeEntity newUser = new EmployeeEntity(user.getName(), user.getEmail());
        empRepository.save(newUser);
        log.info("Employee saved in db: {}", obj.writeValueAsString(newUser));
        EmployeeDTO edto = new EmployeeDTO(newUser.getName(), newUser.getEmail(), newUser.getId());
        log.info("Employee DTO sent: {}", obj.writeValueAsString(edto));
        return edto;
    }

    public List<EmployeeDTO> getAll(){
        return empRepository.findAll().stream().map(entity -> {
            EmployeeDTO edto = new EmployeeDTO(entity.getName(), entity.getEmail(), entity.getId());
            return edto;
        }).collect(Collectors.toList());
    }

    public EmployeeDTO edit(EmployeeDTO user, Long id) throws Exception{
        EmployeeEntity foundEmp = empRepository.findById(id).orElseThrow(()->{
            log.error("Cannot find employee with id : {}", id);
            return new RuntimeException("Can't find employee with this id");
        });
        foundEmp.setName(user.getName());
        foundEmp.setEmail(user.getEmail());
        empRepository.save(foundEmp);
        log.info("Employee saved after editing in db is : {}", obj.writeValueAsString(foundEmp));
        EmployeeDTO edto = new EmployeeDTO(foundEmp.getName(), foundEmp.getEmail(), foundEmp.getId());
        return edto;
    }

    public String delete(Long id){
        EmployeeEntity foundUser = empRepository.findById(id).orElseThrow(()->{
            log.error("Cannot find user with id : {}", id);
            return new RuntimeException("Can't find user with this id");
        });
        empRepository.delete(foundUser);
        return "Deleted successfully";
    }

    public String clear(){
        empRepository.deleteAll();
        return "Database cleared";
    }
}