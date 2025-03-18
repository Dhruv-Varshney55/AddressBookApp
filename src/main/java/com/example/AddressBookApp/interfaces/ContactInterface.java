package com.example.AddressBookApp.interfaces;

import java.util.*;
import com.example.AddressBookApp.dto.ContactDTO;
import com.example.AddressBookApp.dto.ResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ContactInterface {
    public ContactDTO get(Long id);
    public ContactDTO create(ContactDTO user);
    public String clear();
    public List<ContactDTO> getAll();
    public ContactDTO edit(ContactDTO user, Long id);
    public String delete(Long id);
    public ResponseDTO response(String message, String status);
}