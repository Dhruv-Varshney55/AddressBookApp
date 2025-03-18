package com.example.AddressBookApp.serviceTest;

import com.example.AddressBookApp.dto.AuthUserDTO;
import com.example.AddressBookApp.dto.LoginDTO;
import com.example.AddressBookApp.dto.PassDTO;
import com.example.AddressBookApp.entity.AuthUser;
import com.example.AddressBookApp.interfaces.AuthInterface;
import com.example.AddressBookApp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    AuthInterface authInterface;

    @Autowired
    UserRepository userRepository;

    @BeforeEach //setup
    public void registerDummyUser(){
        //clear the test db for fresh start
        authInterface.clear();

        //creating a dummy user
        AuthUserDTO dummyUser = new AuthUserDTO("DummyFirstName", "DummyLastName", "DummyEmail@gmail.com", "DummyPass12#");

        //registering a dummy user, so we can implement other test cases
        authInterface.register(dummyUser);

    }

    @AfterEach //clear test db
    public void clearDb(){
        authInterface.clear();
    }

    @Test
    public void registerTest(){
        // arrange --> action --> assert

        //arrange
        AuthUserDTO tempUser = new AuthUserDTO("FirstName", "LastName", "TempEmail@gmail.com", "Temppass12#");

        //action
        String res = authInterface.register(tempUser);

        //assert
        assertEquals("User registered", res, "User registration test failed");

    }

    @Test
    public void loginTest(){
        //arrange
        LoginDTO userLogin = new LoginDTO("DummyEmail@gmail.com", "DummyPass12#");

        MockHttpServletResponse response = new MockHttpServletResponse();

        //act
        String res = authInterface.login(userLogin, response);

        //res should not be null
        assertNotNull(res);

        //user should be logged in
        assertTrue(res.contains("User logged in"), "Log in test failure");

        //checking the creation of token in cookies of response
        String jwtToken = response.getHeader("Authorization");

        System.out.println(jwtToken);

        assertNotNull(jwtToken, "JWT Token should be set in response");

    }

    @Test
    public void forgotPasswordTest(){
        PassDTO newPass = new PassDTO("Hello1@$");
        AuthUserDTO resDto = authInterface.forgotPassword(newPass, "DummyEmail@gmail.com");
        assertNotNull(resDto);
        assertEquals("Hello1@$", resDto.getPassword(), "Password did not match after forgot");
    }

    @Test
    public void resetPasswordTest(){
        String email = "DummyEmail@gmail.com";
        String currentPass = "DummyPass12#";
        String newPass = "NewPass12#";

        String res = authInterface.resetPassword(email, currentPass, newPass);

        //checking response
        assertEquals("Password reset successful!", res);

        AuthUser foundUser = userRepository.findByEmail("DummyEmail@gmail.com");

        //checking the password updation
        assertEquals("NewPass12#", foundUser.getPassword());

    }
}