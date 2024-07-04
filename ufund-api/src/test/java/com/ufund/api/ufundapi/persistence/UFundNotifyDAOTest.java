package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Notification;

@Tag("Persistence-tier")
public class UFundNotifyDAOTest {
    UFundNotifyDAO ufund;
    Notification[] notifications;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setUp() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        notifications = new Notification[5];

        notifications[0] = new Notification(0, "title 1", "content 1");
        notifications[1] = new Notification(1, "title 2", "content 2");
        notifications[2] = new Notification(2, "Need Edited", "such and such was edited");
        notifications[3] = new Notification(3, "hi", "such and such was deleted");
        notifications[4] = new Notification(4, "idk", "content 3");

        when(mockObjectMapper.readValue(new File("testing"), Notification[].class))
            .thenReturn(notifications);
        ufund = new UFundNotifyDAO("testing", mockObjectMapper);
    }

    @Test
    public void testGetNotifications(){
        Notification[] testNotifications = ufund.getNotifications();

        assertEquals(notifications.length, testNotifications.length);
        for (int i = 0; i < testNotifications.length; i++){
            assertEquals(notifications[i], testNotifications[i]);
        }
    }

    @Test
    public void testFindNotifications(){
        Notification[] filteredNotifications = ufund.findNotifications("content");

        assertEquals(3, filteredNotifications.length);
        assertEquals(notifications[0], filteredNotifications[0]);
        assertEquals(notifications[1], filteredNotifications[1]);
        assertEquals(notifications[4], filteredNotifications[2]);
    }

    @Test
    public void testGetNotification(){
        Notification notification = ufund.getNotification(3);

        assertEquals(notifications[3], notification);

        assertNull(ufund.getNotification(203));
    }

    @Test
    public void testCreateNotification() throws IOException{
        Notification newNotification = new Notification(5, "title 5", "content 5");

        Notification createdNotification = ufund.createNotification(newNotification);

        assertEquals(newNotification, createdNotification);

        // Check if the new notification is in the list of notifications
        Notification[] updatedNotifications = ufund.getNotifications();
        assertEquals(6, updatedNotifications.length);
        assertEquals(newNotification, updatedNotifications[5]);
    }

    @Test
    public void testUpdateNotification() throws IOException{
        Notification updatedNotification = new Notification(2, "Updated Title", "Updated Content");

        Notification result = ufund.updateNotification(updatedNotification);

        assertEquals(updatedNotification, result);

        // Check if the notification was updated in the list of notifications
        Notification[] updatedNotifications = ufund.getNotifications();
        assertEquals(updatedNotification, updatedNotifications[2]);

        assertNull(ufund.updateNotification(new Notification(500, "", "")));
    }

    @Test
    public void testDeleteNotification() throws IOException{
        boolean result = ufund.deleteNotification(1);

        assertEquals(true, result);

        // Check if the notification was deleted from the list of notifications
        Notification[] updatedNotifications = ufund.getNotifications();
        assertEquals(4, updatedNotifications.length);
        assertEquals(notifications[0], updatedNotifications[0]);
        assertEquals(notifications[2], updatedNotifications[1]);
        assertEquals(notifications[3], updatedNotifications[2]);
        assertEquals(notifications[4], updatedNotifications[3]);

        assertFalse(ufund.deleteNotification(230));
    }
}
