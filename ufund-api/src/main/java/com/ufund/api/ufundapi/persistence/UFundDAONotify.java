package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Notification;

/**
 * Defines the interface for Need object persistence
 */
public interface UFundDAONotify {

    /**
     * Retrieves a notification by its ID.
     * @param id The ID of the notification to retrieve.
     * @return The notification with the specified ID.
     * @throws IOException If an IO error occurs while retrieving the notification.
     */
    public Notification getNotification(int id) throws IOException;

    /**
     * Updates an existing notification with new data.
     * @param newNotification The updated notification object.
     * @return The updated notification object.
     * @throws IOException If an IO error occurs while updating the notification.
     */
    public Notification updateNotification(Notification newNotification) throws IOException;

    /**
     * Creates a new notification.
     * @param notify The notification object to create.
     * @return The newly created notification object.
     * @throws IOException If an IO error occurs while creating the notification.
     */
    public Notification createNotification(Notification notify) throws IOException;

    /**
     * Deletes a notification by its ID.
     * @param id The ID of the notification to delete.
     * @return True if the deletion was successful, false otherwise.
     * @throws IOException If an IO error occurs while deleting the notification.
     */
    public boolean deleteNotification(int id) throws IOException;

    /**
     * Retrieves an array of all notifications.
     * @return An array containing all notifications.
     */
    public Notification[] getNotifications();

    /**
     * Finds notifications containing the specified text.
     * @param containsText The text to search for in notifications.
     * @return An array of notifications containing the specified text.
     */
    public Notification[] findNotifications(String containsText);
}
