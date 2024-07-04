package com.ufund.api.ufundapi.model;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class NotificationTest {

    private Notification[] notifications;

    @BeforeEach
    public void setupNeed() throws IOException{
        notifications = new Notification[5];
        notifications[0] = new Notification(0, "Hello world", "Content");
        notifications[1] = new Notification(1, "Title 1", "content 2");
        notifications[2] = new Notification(-2, "Thats me", "another thing");
        notifications[3] = new Notification(3, "thats me", "Content 3");
        notifications[4] = new Notification(5, "Title 4", "Content 4");
    }

    @Test
    public void testGetId() {
        assertEquals(0, notifications[0].getId());
        assertEquals(1, notifications[1].getId());
        assertEquals(-2, notifications[2].getId());
        assertEquals(3, notifications[3].getId());
        assertEquals(5, notifications[4].getId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Hello world", notifications[0].getTitle());
        assertEquals("Title 1", notifications[1].getTitle());
        assertEquals("Thats me", notifications[2].getTitle());
        assertEquals("thats me", notifications[3].getTitle());
        assertEquals("Title 4", notifications[4].getTitle());
    }

    @Test
    public void testGetContent() {
        assertEquals("Content", notifications[0].getContent());
        assertEquals("content 2", notifications[1].getContent());
        assertEquals("another thing", notifications[2].getContent());
        assertEquals("Content 3", notifications[3].getContent());
        assertEquals("Content 4", notifications[4].getContent());
    }

    @Test
    public void testGetTime() {
        // You may not be able to test the exact creation time, as it changes with each instance.
        // You can only verify if the method returns a non-null string.
        assertNotNull(notifications[0].getTime());
        assertNotNull(notifications[1].getTime());
        assertNotNull(notifications[2].getTime());
        assertNotNull(notifications[3].getTime());
        assertNotNull(notifications[4].getTime());
    }

    @Test
    public void testHashCode(){
        assertEquals(0, notifications[0].hashCode());
        assertEquals(1, notifications[1].hashCode());
        assertEquals(-2, notifications[2].hashCode());
        assertEquals(3, notifications[3].hashCode());
        assertEquals(5, notifications[4].hashCode());
    }

    @Test
    public void testEquals(){
        assertTrue(notifications[0].equals(new Notification(0, "Title", "Content")));
        assertFalse(notifications[0].equals(new Notification(10, "Hello world", "Content")));
        assertFalse(notifications[0].equals(null));
        assertFalse(notifications[0].equals("Not a Notification object"));
    }

    @Test
    public void testConstructorNullTitleContent() {
        assertThrows(NullPointerException.class, () -> new Notification(5, null, null));
        assertThrows(NullPointerException.class, () -> new Notification(0, "Test", null));
    }
}
