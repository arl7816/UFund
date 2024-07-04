package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

/**
 * Contains the information about a notification within the system
 */
public class Notification {
    /** LOGS curl commands captured */
    private static final Logger LOG = Logger.getLogger(Notification.class.getName());

    /** Format for a given notification */
    static final String STRING_FORMAT = "Notification [id=%d, title=%s]";
    /** A unqiue number used to identify the notification */
    @JsonProperty("id") private int id;
    /** the title of the notification */
    @JsonProperty("title") private String title;
    /** The content of the notification */
    @JsonProperty("content") private String content;
    /** The time the notification was created in the form of (dd-MM-yyyy HH:mm:ss) */
    @JsonProperty("creation") private String creation;

    
    /**
     * Creates a given notification
     * @param id the id of the notification
     * @param title the title of the notification
     * @param content the actual content of the notification
     */
    public Notification(@JsonProperty("id") int id, @JsonProperty("title") String title, @JsonProperty("content") String content){
        if (title == null || content == null){
            throw new NullPointerException("Either title or content is null");
        }

        this.id = id;
        this.title = title;
        this.content = content;

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        this.creation = myDateObj.format(myFormatObj);
    }

    /**
     * gets the id of the notification
     * @return the id
     */
    public int getId(){ return this.id; }
    /***
     * gets the title of the notification
     * @return the title
     */
    public String getTitle(){ return this.title; }
    /**
     * gets the content of the notification
     * @return the content
     */
    public String getContent(){ return this.content; }
    /**
     * Gets the creation time of the notification
     * @return the creation time
     */
    public String getTime(){ return this.creation; }

    /**
     * gets the hashcode of the notification
     * @return the hashcode as the id
     */
    @Override
    public int hashCode(){
        return this.id;
    }

    /**
     * determines if notifications are equal to eachother
     * @return true if both have the same id, false otherwise
     */
    @Override
    public boolean equals(Object other){
        boolean result = false;
        if (other instanceof Notification){
            Notification otherNotify = (Notification)other;
            result = otherNotify.id == this.id;
        }
        return result;
    }
}
