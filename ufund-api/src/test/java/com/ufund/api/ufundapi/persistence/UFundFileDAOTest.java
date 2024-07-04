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
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

@Tag("Persistence-tier")
public class UFundFileDAOTest {
    UFundDAO ufund;
    Need[] needs;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setUp() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        needs = new Need[5];

        needs[0] = new Need(0, "title 1", 1);
        needs[1] = new Need(1, "title 2", 2);
        needs[2] = new Need(2, "Need Edited", 3);
        needs[3] = new Need(3, "hi", 5);
        needs[4] = new Need(4, "idk", 7);

        when(mockObjectMapper.readValue(new File("testing"), Need[].class))
            .thenReturn(needs);
        ufund = new UFundFileDAO("testing", mockObjectMapper);
    }

    @Test
    public void testGetNeeds() throws IOException{
        Need[] testNeeds = ufund.getNeeds();

        assertEquals(needs.length, testNeeds.length);
        for (int i = 0; i < testNeeds.length; i++){
            assertEquals(needs[i], testNeeds[i]);
        }
    }

    @Test
    public void testFindNeeds() throws IOException{
        Need[] filteredNeeds = ufund.findNeeds("title");

        assertEquals(2, filteredNeeds.length);
        assertEquals(needs[0], filteredNeeds[0]);
        assertEquals(needs[1], filteredNeeds[1]);
    }

    @Test
    public void testGetNeed() throws IOException{
        Need notification = ufund.getNeed(3);

        assertEquals(needs[3], notification);

        assertNull(ufund.getNeed(203));
    }

    @Test
    public void testCreateNeed() throws IOException{
        Need newNeed = new Need(5, "title 5", 5);

        Need createdNeed = ufund.createNeed(newNeed);

        assertEquals(newNeed, createdNeed);

        // Check if the new notification is in the list of needs
        Need[] updatedNeeds = ufund.getNeeds();
        assertEquals(6, updatedNeeds.length);
        assertEquals(newNeed, updatedNeeds[5]);

        Need nullNeed = ufund.createNeed(new Need(0, null, 0));
        assertNull(nullNeed);

        Need alreadyExist = ufund.createNeed(new Need(0, "title 5", 2));
        assertNull(alreadyExist);
    }

    @Test
    public void testUpdateNeed() throws IOException{
        Need updatedNeed = new Need(2, "Updated Title", 5);

        Need result = ufund.updateNeed(updatedNeed);

        assertEquals(updatedNeed, result);

        // Check if the notification was updated in the list of needs
        Need[] updatedNeeds = ufund.getNeeds();
        assertEquals(updatedNeed, updatedNeeds[2]);

        assertNull(ufund.updateNeed(new Need(500, "", 5)));
    }

    @Test
    public void testDeleteNeed() throws IOException{
        boolean result = ufund.deleteNeed(1);

        assertEquals(true, result);

        // Check if the notification was deleted from the list of needs
        Need[] updatedNeeds = ufund.getNeeds();
        assertEquals(4, updatedNeeds.length);
        assertEquals(needs[0], updatedNeeds[0]);
        assertEquals(needs[2], updatedNeeds[1]);
        assertEquals(needs[3], updatedNeeds[2]);
        assertEquals(needs[4], updatedNeeds[3]);

        assertFalse(ufund.deleteNeed(230));
    }
}
