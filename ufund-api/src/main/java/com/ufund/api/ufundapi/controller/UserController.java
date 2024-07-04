package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.persistence.UFundDAOUsers;

import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;

@RestController
@RequestMapping("users")
public class UserController {
    private UFundDAOUsers dao; // Assume this is an instance of UFundFileDAO
    
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    
    /**
     * Creates a instance of our rest controller for Users
     * @param dao an injected class UFundDAO users to keep persistence
     */
    public UserController(UFundDAOUsers dao){
            this.dao = dao;
    }

    /**
     * Gets a {@linkplain User user} stored in memory via their id
     * @param id the id of the {@linkplain User user}
     * @return a {@linkplain ResponseEntity response entity} of type User.
     * The status along with the entity may include a status of OK, NOT_FOUND, or INTERNAL_SEVER_ERROR
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        /* DONE */
        LOG.info("GET /users/" + id);
        try {
            User user = this.dao.getUser(id);
            if (user != null)
                return new ResponseEntity<User>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
                LOG.log(Level.SEVERE,e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }

    /**
     * Gets a {@linkplain User user} stored in memory via their username
     * @param username the username of the {@linkplain User user}
     * @return a {@linkplain ResponseEntity response entity} of type User.
     * The status along with the entity may include a status of OK, NOT_FOUND, or INTERNAL_SEVER_ERROR
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username){
        LOG.info("GET /users/" + username);
        try {
            //User user = this.dao.getUser(id);
            User[] users = this.dao.getUsers();
            User user = null;
            for (User userTemp: users){
                if (userTemp.getUsername().equals(username)){
                    user = userTemp;
                    break;
                }
            }
            if (user != null)
                return new ResponseEntity<User>(user,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
                LOG.log(Level.SEVERE,e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }

    /**
     * Returns an array of all users within the system
     * @return a {@linkplain ResponseEntity response entity} of a User Array.
     * The status along with the entity may include a status of OK, NOT_FOUND, or INTERNAL_SEVER_ERROR
     */
    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        LOG.info("GET /users");
        
        // Replace below with your implementation
        try {
            User[] user = dao.getUsers();
            if (user != null)
                return new ResponseEntity<User[]>(user,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.info("POST /users " + user);
        try {
            User newUser = dao.createUser(user);
            if (newUser != null)
                return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Inside UserController class

    /**
     * Deletes a user from the system.
     * @param user the user to delete
     * @return ResponseEntity indicating the success or failure of the operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable int id) {
        LOG.info("DELETE /deleteUser");
        // if (user == null) {
        //     // If the user object is null, return a Bad Request status
        //     return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        // }
        //int id = user.getId();
        
        try {
            // Use the DAO to delete the user
            boolean isDeleted = dao.deleteUser(id);
            if (isDeleted) {
                // If the DAO reports successful deletion, return OK status
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                // If the DAO reports failure, return a Not Acceptable status
                // This could mean the user was not found or could not be deleted for some reason
                return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * update a single user based on ID
     * @param user
     * @return a <@linkplain ResponseEntity response entity} of a User
     * status may include status of OK, NOT_FOUND, and INTERNAL_SERVER_ERROR
     */
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        LOG.info("PUT /users " + user);

        try{

                if(user!=null)
                {
                    User refUser = this.dao.getUser(user.getId());
                    if(refUser!=null) {

                        if (user.getUsername() != null){
                            refUser.setUsername(user.getUsername());
                        }
                        
                        if (user.getFundingBasket() != null){
                            refUser.setFundingBasket(user.getFundingBasket());
                        }

                        if (user.getWallet() != 0){
                            refUser.setWallet(user.getWallet());
                        }
                    this.dao.updateUser(user);
                    return new ResponseEntity<User>(user, HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                }
                
            else{

                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    

    @PostMapping("/login")
    public ResponseEntity<User> login (@RequestBody User user){

        ResponseEntity<User> userToLogin = getUser(user.getUsername());
        if (userToLogin!=null){
            if (userToLogin.getBody().getPassword().equals(user.getPassword())){
                return new ResponseEntity<User>(user,HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(user, HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        
    }

    /* NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW */

    /**
     * returns the list of needs in the user's funding basket
     * @param user
     * @return needs
     */
    @GetMapping("/getBasket/{username}")
    public ResponseEntity<List<Need>> getFundingBasket (@PathVariable String username)
    {
        //try {
            User user = getUser(username).getBody();
            if (user != null)
                return new ResponseEntity<List<Need>>(user.getFundingBasket().listNeeds(),HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //}
        /*catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
    }

    /**
     * remove a specific need from a specific user's funding basket
     * @param user
     * @param need
     * @return ResponseEntity<Boolean> indicates true or false success of removal
     */
    @PutMapping("/removeFromBasket/{username}")
    public ResponseEntity<Boolean> removeFromBasket(@PathVariable String username, @RequestBody Need need)
    {

        if (need == null) {
            // If the user object is null, return a Bad Request status
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        User user = getUser(username).getBody();
        
        try {
            if(user!=null)
            {
                boolean result = user.getFundingBasket().removeNeed(need);
                dao.updateUser(user);
                return new ResponseEntity<Boolean>(result, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addToBasket/{username}")
    public ResponseEntity<Boolean> addToBasket (@PathVariable String username, @RequestBody Need need){
        User user = getUser(username).getBody();
        try{
            if(user!=null&&need!=null)
            {
                boolean result =user.getFundingBasket().addNeed(need);
                FundingBasket temp = user.getFundingBasket();
                //temp.addNeed(need);
                user.setFundingBasket(temp);
                dao.updateUser(user);
                return new ResponseEntity<Boolean>(result, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    
}
