package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Notification;
import com.ufund.api.ufundapi.persistence.UFundDAONotify;

@Tag("Controller-tier")
public class NotificationControllerTest {
    private NotificationController controller;
    private UFundDAONotify mockUFund;

    @BeforeEach
    public void setupNotificationController() {
        mockUFund = mock(UFundDAONotify.class);
        controller = new NotificationController(mockUFund);
    }

    @Test
    public void testGetNotification() throws IOException {
        Notification notification = new Notification(2, "hi", "I am content");

        when(mockUFund.getNotification(notification.getId())).thenReturn(notification);

        ResponseEntity<Notification> response = controller.getNotification(notification.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notification, response.getBody());

        when(mockUFund.getNotification(7)).thenReturn(null);
        response = controller.getNotification(7);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        when(mockUFund.getNotification(0)).thenThrow(IOException.class);
        response = controller.getNotification(0);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNotifications() throws IOException {
        Notification[] notifications = {
                new Notification(1, "title1", "content1"),
                new Notification(2, "title2", "content2"),
                new Notification(3, "title3", "content3")
        };

        when(mockUFund.getNotifications()).thenReturn(notifications);

        ResponseEntity<Notification[]> response = controller.getNotifications();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notifications.length, response.getBody().length);
        for (int i = 0; i < notifications.length; i++) {
            assertEquals(notifications[i], response.getBody()[i]);
        }

        when(mockUFund.getNotifications()).thenReturn(null);

        response = controller.getNotifications();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateNotification() throws IOException {
        Notification notification = new Notification(2, "hi", "I am content");

        when(mockUFund.createNotification(notification)).thenReturn(notification);

        ResponseEntity<Notification> response = controller.createNotification(notification);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(notification, response.getBody());

        when(mockUFund.createNotification(null)).thenReturn(null);
        response = controller.createNotification(null);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        when(mockUFund.createNotification(null)).thenThrow(IOException.class);
        response = controller.createNotification(null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
