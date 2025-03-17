package com.example.AddressBookApp.controller;

import com.example.AddressBookApp.dto.EmployeeDTO;
import com.example.AddressBookApp.dto.ResponseDTO;
import com.example.AddressBookApp.interfaces.EmployeeInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController {

    ObjectMapper obj = new ObjectMapper();

    @Autowired
    EmployeeInterface employeeInterface;

    // UC1 (Handling REST API using ResponseDTO without service layer)

    @GetMapping("/res/get/{id}")
    public ResponseDTO get1(@PathVariable Long id){
        log.info("Get id: {}", id);
        return new ResponseDTO("API triggered", "Success");
    }

    @PostMapping("/res/create")
    public ResponseDTO create1(@RequestBody EmployeeDTO user) throws Exception{
        log.info("Create employee: {}", obj.writeValueAsString(user));
        return new ResponseDTO("API triggered", "Success");
    }

    @GetMapping("/res/getAll")
    public ResponseDTO getAll1(){
        log.info("Get all employees");
        return new ResponseDTO("API triggered", "Success");
    }

    @PutMapping("/res/edit/{id}")
    public ResponseDTO edit1(@RequestBody EmployeeDTO user, @PathVariable Long id) throws Exception{
        log.info("Edit id: {} and body: {}", id, obj.writeValueAsString(user));
        return new ResponseDTO("API triggered", "Success");
    }

    @DeleteMapping("/res/delete/{id}")
    public ResponseDTO delete1(@PathVariable Long id){
        log.info("Delete id: {}", id);
        return new ResponseDTO("API triggered", "Success");
    }






    //UC2 (Handling REST API using service layer without database)

    @GetMapping("/res2/get/{id}")
    public ResponseDTO get2(@PathVariable Long id){
        log.info("Get id: {}", id);
        return employeeInterface.res("API triggered", "Success");
    }

    @PostMapping("/res2/create")
    public ResponseDTO create2(@RequestBody EmployeeDTO user) throws Exception{
        log.info("Create employee: {}", obj.writeValueAsString(user));
        return employeeInterface.res("API triggered", "Success");
    }

    @GetMapping("/res2/getAll")
    public ResponseDTO getAll2(){
        log.info("Get all employee");
        return employeeInterface.res("API triggered", "Success");
    }

    @PutMapping("/res2/edit/{id}")
    public ResponseDTO edit2(@RequestBody EmployeeDTO user, @PathVariable Long id) throws Exception{
        log.info("Edit id: {} and body: {}", id, obj.writeValueAsString(user));
        return employeeInterface.res("API triggered", "Success");
    }

    @DeleteMapping("/res2/delete/{id}")
    public ResponseDTO delete2(@PathVariable Long id){
        log.info("Delete id: {}", id);
        return employeeInterface.res("API triggered", "Success");
    }






    //UC3 (Handling REST API using service layer with database)

    @GetMapping("/get/{id}")
    public EmployeeDTO get3(@PathVariable Long id) throws Exception {
        log.info("Get id: {}", id);
        return employeeInterface.get(id);
    }

    @PostMapping("/create")
    public EmployeeDTO create3(@RequestBody EmployeeDTO user) throws Exception{
        log.info("Create employee: {}", obj.writeValueAsString(user));
        return employeeInterface.create(user);
    }

    @GetMapping("/getAll")
    public List<EmployeeDTO> getAll3(){
        log.info("Get all employees");
        return employeeInterface.getAll();
    }

    @PutMapping("/edit/{id}")
    public EmployeeDTO edit3(@RequestBody EmployeeDTO user, @PathVariable Long id) throws Exception{
        log.info("Edit id: {} and body: {}", id, obj.writeValueAsString(user));
        return employeeInterface.edit(user, id);
    }

    @DeleteMapping("/delete/{id}")
    public String delete3(@PathVariable Long id){
        log.info("Delete id: {}", id);
        return employeeInterface.delete(id);
    }

    @GetMapping("/clear")
    public String clear(){
        return employeeInterface.clear();
    }
}