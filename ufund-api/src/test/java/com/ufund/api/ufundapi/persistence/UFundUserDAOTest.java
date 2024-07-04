package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.ufund.api.ufundapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
public class UFundUserDAOTest {
    UfundUserDAO UFundUserDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;



    @BeforeEach
    public void setupUFundUserDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[3];
        testUsers[0] = new User(1,"phil",null,"password");
        testUsers[1] = new User(2,"jimmy",null,"password");
        testUsers[2] = new User(3,"jack",null,"password");

        when(mockObjectMapper
            .readValue(new File("testing"),User[].class))
                .thenReturn(testUsers);
        UFundUserDAO = new UfundUserDAO("testing",mockObjectMapper);
    }

    @Test
    public void testGetusers() throws NullPointerException, IOException{
        // Invoke
        User[] users = UFundUserDAO.getUsers();
        // Analyze
        assertEquals(users.length,testUsers.length);
        for (int i = 0; i < testUsers.length;++i)
           assertEquals(users[i],testUsers[i]);
    }

    @Test
    public void testFindusers() throws IOException{
        // Invoke
        User[] users = UFundUserDAO.findUsers("j");

        // Analyze
        assertEquals(2,users.length);
        assertEquals(users[0],testUsers[1]);
        assertEquals(users[1],testUsers[2]);
    }

    @Test
    public void testGetUser() throws IOException {
        // Invoke
        User user = UFundUserDAO.getUser(1);

        // Analzye
        assertEquals(user,testUsers[0]);
    }

    @Test
    public void testDeleteUser() throws NullPointerException {
        // Invoke
        boolean result = assertDoesNotThrow(() -> UFundUserDAO.deleteUser(2),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(true,result);
        // We check the internal tree map size against the length
        // of the test users array - 1 (because of the delete)
        // Because users attribute of UserFileDAO is package private
        // we can access it directly
        assertEquals(UFundUserDAO.users.size(),testUsers.length-1);
    }

    @Test
    public void testCreateUser() throws IOException {
        // Setup
        User user = new User(4,"jones",null,"password");

        // Invoke
        User result = assertDoesNotThrow(() -> UFundUserDAO.createUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = UFundUserDAO.getUser(user.getId());
        assertEquals(actual.getId(),user.getId());
        assertEquals(actual.getUsername(),user.getUsername());
    }

    @Test
    public void testUpdateUser() throws IOException {
        // Setup
        User user = new User(1,"jimmy",null,"password");

        // Invoke
        User result = assertDoesNotThrow(() -> UFundUserDAO.updateUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = UFundUserDAO.getUser(user.getId());
        assertEquals(actual,user);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));

        User user = new User(4,"jones",null,"password");

        assertThrows(IOException.class,
                        () -> UFundUserDAO.createUser(user),
                        "IOException not thrown");
    } 

    @Test
    public void testGetUserNotFound() throws IOException {
        // Invoke
        User user = UFundUserDAO.getUser(-1);

        // Analyze
        assertEquals(user,null);
    }

    @Test
    public void testDeleteUserNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> UFundUserDAO.deleteUser(6),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(UFundUserDAO.users.size(),testUsers.length);
    }

    @Test
    public void testUpdateUserNotFound() {
        // Setup
        User user = new User(-1,"phil", null,"password");

        // Invoke
        User result = assertDoesNotThrow(() -> UFundUserDAO.updateUser(user),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the UserFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("testing"),User[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new UfundUserDAO("testing",mockObjectMapper),
                        "IOException not thrown");
    }
}
