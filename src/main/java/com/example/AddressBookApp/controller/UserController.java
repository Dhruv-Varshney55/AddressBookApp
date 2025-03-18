package com.example.AddressBookApp.controller;

import com.example.AddressBookApp.dto.*;
import com.example.AddressBookApp.interfaces.AuthInterface;
import com.example.AddressBookApp.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class UserController {
    @Autowired
    EmailService emailService;

    @Autowired
    AuthInterface authInterface;

    // Register
    @PostMapping(path = "/register")
    public String register(@Valid @RequestBody AuthUserDTO user){
        log.info("Register: {}", getJSON(user));
        return authInterface.register(user);
    }

    // Login
    @PostMapping(path ="/login")
    public String login(@Valid @RequestBody LoginDTO user, HttpServletResponse response){
        log.info("Login: {}", getJSON(user));
        return authInterface.login(user, response);
    }

    // Send mail
    @PostMapping(path = "/sendMail")
    public String sendMail(@Valid @RequestBody MailDTO message){
        log.info("Send email: {}", getJSON(message));
        emailService.sendEmail(message.getTo(), message.getSubject(), message.getBody());
        return "Mail sent";
    }

    // Forgot password
    @PutMapping("/forgotPassword/{email}")
    public AuthUserDTO forgotPassword(@Valid @RequestBody PassDTO pass, @Valid @PathVariable String email){
        log.info("Forgot password: {}", getJSON(pass));
        return authInterface.forgotPassword(pass, email);
    }

    // Reset password
    @PutMapping("/resetPassword/{email}")
    public String resetPassword(@Valid @PathVariable String email, @Valid @RequestBody Map<String, String> requestBody){
        String currentPass = requestBody.get("currentPass");
        String newPass = requestBody.get("newPass");
        log.info("Reset password request for email: {}", email);
        return authInterface.resetPassword(email, currentPass, newPass);
    }

    @GetMapping("/clear")
    public String clear(){
        log.info("Database cleared");
        return authInterface.clear();
    }

    public String getJSON(Object object){
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.writeValueAsString(object);
        }
        catch(JsonProcessingException e){
            log.error("Reason: {} Exception: {}", "Conversion error from Java Object to JSON");
        }
        return null;
    }
}