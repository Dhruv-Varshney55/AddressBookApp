package com.example.AddressBookApp.interfaces;

import com.example.AddressBookApp.dto.AuthUserDTO;
import com.example.AddressBookApp.dto.LoginDTO;
import com.example.AddressBookApp.dto.PassDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthInterface {
    public String register (AuthUserDTO user);
    public String login(LoginDTO user, HttpServletResponse response);
    public AuthUserDTO forgotPassword(PassDTO pass, String email);
    public String resetPassword(String email, String currentPass, String newPass);
    public String clear();
}