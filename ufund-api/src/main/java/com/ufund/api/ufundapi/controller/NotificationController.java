package com.ufund.api.ufundapi.controller;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Notification;
import com.ufund.api.ufundapi.persistence.UFundDAO;
import com.ufund.api.ufundapi.persistence.UFundDAONotify;

import java.io.IOException;
import java.util.Arrays;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controls the curl commands for notifications
 */
@RestController
@RequestMapping("notify")
public class NotificationController {
    
        /** The DAO used by the controller*/
        private UFundDAONotify dao; // Assume this is an instance of UFundFileDAO
        private static final Logger LOG = Logger.getLogger(NotificationController.class.getName());

        /**
         * <<Constructor>> for the controller
         * @param dao the dao used for the controller's curl commands
         */
        public NotificationController(UFundDAONotify dao){
            this.dao = dao;
        }
    
        /**
         * creates a new notification and saves it into memory
         * 
         * 
         * @param notify the new notification to be added to the memory. NOTE: the id of the given is replaced by a unqiue id
         * @return the new notification
         */
        @PostMapping("")
        public ResponseEntity<Notification> createNotification(@RequestBody Notification notify) {
            LOG.info("POST /ufund " + notify);
            try {
                Notification newNotify = dao.createNotification(notify);
                if (newNotify != null)
                    return new ResponseEntity<Notification>(newNotify, HttpStatus.CREATED);
                else
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            catch(IOException e) {
                LOG.log(Level.SEVERE,e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        /**
         * gets a notification based on the id
         * @param id the identifier of the given notification
         * @return a response entity containing the notification if found, otherwise an empty one
         */
        @GetMapping("/{id}")
        public ResponseEntity<Notification> getNotification(@PathVariable int id) {
            /* DONE */
            LOG.info("GET /notify/" + id);
            try {
            Notification notify = this.dao.getNotification(id);
            if (notify != null)
                return new ResponseEntity<Notification>(notify,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            catch(IOException e) {
                    LOG.log(Level.SEVERE,e.getLocalizedMessage());
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } 
        }

        /**
         * Gets all notification within memory
         * @return a repsonse entity with all notification, empty response otherwise
         */
        @GetMapping("/")
        public ResponseEntity<Notification[]> getNotifications() {
            /* DONE */
            LOG.info("GET /notify/");
            Notification[] notify = this.dao.getNotifications();
            if (notify != null)
                return new ResponseEntity<Notification[]>(notify,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
