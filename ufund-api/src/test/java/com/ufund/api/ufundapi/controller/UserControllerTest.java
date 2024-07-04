package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.persistence.UFundDAOUsers;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

public class UserControllerTest {
    private UserController userController;
    private UserController mockController;
    private UFundDAOUsers daoUsers;

    @BeforeEach
    public void setup(){
        daoUsers = mock(UFundDAOUsers.class);
        mockController = mock(UserController.class); // mock controller
        userController = new UserController(daoUsers);
    }

    @Test
    public void testGetUser() throws IOException {
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        //userController.createUser(user);
        when(daoUsers.getUser(user.getId())).thenReturn(user);

        //invoke
        ResponseEntity<User> response =  userController.getUser(user.getId());

        //analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        when(daoUsers.getUser(23)).thenReturn(null);
        response =  userController.getUser(23);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        when(daoUsers.getUser(40)).thenThrow(IOException.class);
        response =  userController.getUser(40);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    public void testGetUserNotFound() throws IOException {
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(5, "admin", basket, "admin");
        //userController.createUser(user);
        when(daoUsers.getUser(user.getId())).thenReturn(null);

        //invoke
        ResponseEntity<User> response =  userController.getUser(user.getId());

        //analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

     @Test
    public void testGetUserHandleException() throws Exception { // createNeed may throw IOException
        // Setup
        FundingBasket basket = new FundingBasket();
        User user = new User(5, "admin", basket, "admin");
        // When getNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(daoUsers).getUser(user.getId());

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getId());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUsers() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        User user2 = new User(1, "hi", basket, "hello");
        User[] users = {user, user2};
        when(daoUsers.getUsers()).thenReturn(users);

        //invoke
        ResponseEntity<User[]> response = userController.getUsers();

        //analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUsersNotFound() throws IOException{
        //setup
        when(daoUsers.getUsers()).thenReturn(null);

        //invoke
        ResponseEntity<User[]> response = userController.getUsers();

        //analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUsersHandleException() throws IOException{
        //setup
        doThrow(new IOException()).when(daoUsers).getUsers();

        //invoke
        ResponseEntity<User[]> response = userController.getUsers();

        //analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateUser() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        //userController.createUser(user);
        when(daoUsers.createUser(user)).thenReturn(user);

        //invoke
        ResponseEntity<User> response = userController.createUser(user);

        //analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateUserConflict() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        //userController.createUser(user);
        when(daoUsers.createUser(user)).thenReturn(null);

        //invoke
        ResponseEntity<User> response = userController.createUser(user);

        //analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        //userController.createUser(user);
        doThrow(new IOException()).when(daoUsers).createUser(user);

        //invoke
        ResponseEntity<User> response = userController.createUser(user);

        //analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        when(daoUsers.deleteUser(user.getId())).thenReturn(true);

        //invoke
        ResponseEntity<Boolean> reponse = userController.deleteUser(user.getId());

        //analyze
        assertEquals(HttpStatus.OK, reponse.getStatusCode());
    }

    @Test
    public void testDeleteUserNotAcceptable() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        when(daoUsers.deleteUser(user.getId())).thenReturn(false);

        //invoke
        ResponseEntity<Boolean> reponse = userController.deleteUser(user.getId());

        //analyze
        assertEquals(HttpStatus.NOT_ACCEPTABLE, reponse.getStatusCode());
    }

    @Test
    public void testDeleteUserError() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        doThrow(new IOException()).when(daoUsers).deleteUser(user.getId());

        //invoke
        ResponseEntity<Boolean> reponse = userController.deleteUser(user.getId());

        //analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, reponse.getStatusCode());
    }

    @Test
    public void testUpdateUser() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        when(daoUsers.getUser(user.getId())).thenReturn(user);
        ResponseEntity<User> response = userController.updateUser(user);
        user.setUsername("hello");

        //invoke
        response = userController.updateUser(user);

        //analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUserNotFound() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        when(daoUsers.getUser(user.getId())).thenReturn(null);
        ResponseEntity<User> response = userController.updateUser(user);
        user.setUsername("hello");

        //invoke
        response = userController.updateUser(user);

        //analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUserHandleError() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        doThrow(new IOException()).when(daoUsers).getUser(user.getId());
        

        //invoke
        ResponseEntity<User> response = userController.updateUser(user);

        //analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLogin() throws IOException{
        //setup
        FundingBasket basket = new FundingBasket();
        User user = new User(4, "admin", basket, "admin");
        daoUsers.createUser(user);
        User[] usersArray = new User[]{ user };
        when(daoUsers.getUsers()).thenReturn(usersArray);
        when(daoUsers.getUser(user.getId())).thenReturn(user);

        //invoke
        ResponseEntity<User> response = userController.login(user);

        //analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLoginConflict() throws IOException{
       // Setup
       FundingBasket basket = new FundingBasket();
       User storedUser = new User(4, "admin", basket, "admin"); // This is the user in the database with the correct password
       User loginUser = new User(4, "admin", basket, "no"); // This is the user trying to log in with the wrong password
   
       User[] usersArray = new User[]{ storedUser };
       when(daoUsers.getUsers()).thenReturn(usersArray);
       when(daoUsers.getUser(loginUser.getId())).thenReturn(storedUser); // When login is attempted, the stored user (with the correct password) is returned
   
       // Invoke
       ResponseEntity<User> response = userController.login(loginUser);
   
       // Analyze
       assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }   
    
    // @Test
    // public void testGetFundingBasket() throws IOException {
    //     // Setup
    //     String username = "testUser";
    //     FundingBasket basket = new FundingBasket();
    //     User user = new User(1, username, basket, "password");

    //     // Define mock behavior
    //     when(mockController.getUser(username)).thenReturn(new ResponseEntity<>(user, HttpStatus.OK));

    //     // Invoke
    //     ResponseEntity<List<Need>> response = userController.getFundingBasket(username);

    //     // Analyze
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(basket.listNeeds(), response.getBody());
    // }

    @Test
    public void testGetUserExistingUser() throws IOException {
        // Setup
        int userId = 1;
        User user = new User(userId, "testUser", new FundingBasket(), "password");
        when(daoUsers.getUser(userId)).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserNonExistingUser() throws IOException {
        // Setup
        int userId = 1;
        when(daoUsers.getUser(userId)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserIOException() throws IOException {
        // Setup
        int userId = 1;
        when(daoUsers.getUser(userId)).thenThrow(IOException.class);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
