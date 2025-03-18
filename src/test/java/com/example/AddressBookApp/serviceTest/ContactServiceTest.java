package com.example.AddressBookApp.serviceTest;

import com.example.AddressBookApp.dto.AuthUserDTO;
import com.example.AddressBookApp.dto.ContactDTO;
import com.example.AddressBookApp.dto.LoginDTO;
import com.example.AddressBookApp.entity.ContactEntity;
import com.example.AddressBookApp.interfaces.AuthInterface;
import com.example.AddressBookApp.interfaces.ContactInterface;
import com.example.AddressBookApp.repository.ContactRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContactServiceTest {

    @Autowired
    AuthInterface authInterface;

    @Autowired
    ContactInterface contactInterface;

    @Autowired
    ContactRepository contactRepository;

    MockHttpServletRequest request = new MockHttpServletRequest();

    Long id = -1L;

    @BeforeEach //setup
    public void registerAndLoginDummyUser(){
        //clear the test db for fresh start
        authInterface.clear();
        contactInterface.clear();

        //creating a dummy user
        AuthUserDTO dummyUser = new AuthUserDTO("DummyFirstName", "DummyLastName", "DummyEmail@gmail.com", "DummyPass12#");

        //registering a dummy user, so we can implement other test cases
        authInterface.register(dummyUser);

        //logging in the dummy user
        LoginDTO userLogin = new LoginDTO("DummyEmail@gmail.com", "DummyPass12#");

        MockHttpServletResponse response = new MockHttpServletResponse();

        authInterface.login(userLogin, response);

        String auth = response.getHeader("Authorization");

        request.addHeader("Authorization", auth);
    }

    @AfterEach
    public void clearDb(){
        authInterface.clear();
        contactInterface.clear();
    }

    @Test
    public void createTest(){
        ContactDTO newContact = new ContactDTO("Dhruv Varshney", "dhruv@gmail.com", 843249724L, "Hathras");

        ContactDTO resDto = contactInterface.create(newContact, request);

        assertNotNull(resDto);

        //finding the contact in db
        ContactEntity foundContact = contactRepository.findByEmail("dhruv@gmail.com");

        assertNotNull(foundContact);

        assertEquals(843249724L, foundContact.getPhoneNumber());
    }

    @Test
    public void getTest(){
        //creating a contact
        ContactDTO newContact = new ContactDTO("Dhruv Varshney", "dhruv@gmail.com", 843249724L, "Hathras");
        ContactDTO resDto = contactInterface.create(newContact, request);
        assertNotNull(resDto);

        //action
        ContactDTO getDto = contactInterface.get(resDto.getId(), request);

        //assert
        assertEquals(getJSON(resDto), getJSON(getDto));
    }

    @Test
    public void getAllTest(){
        //creating a list of contacts to add in the contact db one at a time
        List<ContactDTO> l1 = new ArrayList<>();

        l1.add(new ContactDTO("Dhruv Varshney", "dhruv@gmail.com", 843249724L, "Hathras"));
        l1.add(new ContactDTO("Vikas Singh", "vikas@gmail.com", 783573595L, "Aligarh"));
        l1.add(new ContactDTO("Ashwani Sahu", "ashwani@gmail.com", 973847535L, "Etah"));

        //adding the contacts to test db and setting the response dtos in the original list
        for (int i=0 ; i<l1.size() ; i++) {
            l1.set(i,contactInterface.create(l1.get(i), request));
        }

        List<ContactDTO> resList = contactInterface.getAll(request);

        //check all the contacts are received or not
        for(int i = 0;i<l1.size();i++){
            assertEquals(getJSON(l1.get(i)), getJSON(resList.get(i)));
        }
    }

    @Test
    public void editTest(){
        //creating a contact
        ContactDTO newContact = new ContactDTO("Dhruv Varshney", "dhruv@gmail.com", 843249724L, "Hathras");

        ContactDTO resDto1 = contactInterface.create(newContact, request);

        assertNotNull(resDto1);

        //edit the previous contact wit new one
        ContactDTO editContact = new ContactDTO("Aman Verma", "aman@gmail.com", 984534535L, "Aligarh", resDto1.getId());

        ContactDTO resDto2 = contactInterface.edit(editContact, resDto1.getId(), request);

        //assert the changes made with intended changes
        assertEquals(getJSON(editContact), getJSON(resDto2));
    }

    @Test
    public void deleteTest(){
        //creating a contact
        ContactDTO newContact = new ContactDTO("Dhruv Varshney", "dhruv@gmail.com", 843249724L, "Hathras");
        ContactDTO resDto1 = contactInterface.create(newContact, request);
        assertNotNull(resDto1);

        //delete the contact created
        String res = contactInterface.delete(resDto1.getId(), request);

        //match the res with expected response
        assertEquals("Contact deleted", res, "Response didn't match");

        //double check in db if removed or not
        ContactEntity foundContact = contactRepository.findByEmail("dhruv@gmail.com");

        //foundContact should be null if removed from test db
        assertNull(foundContact);
    }

    public String getJSON(Object object){
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.writeValueAsString(object);
        }
        catch(JsonProcessingException e){
            System.out.println("Exception : Conversion error from Java Object to JSON");
        }
        return null;
    }
}