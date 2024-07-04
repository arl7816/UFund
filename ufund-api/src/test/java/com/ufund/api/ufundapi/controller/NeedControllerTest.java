package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ufund.api.ufundapi.persistence.UFundBundleDAO;
import com.ufund.api.ufundapi.persistence.UFundDAO;
import com.ufund.api.ufundapi.model.Need;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private UFundDAO mockUFundDAO;
    private UFundBundleDAO mockBundleDAO;

    /**
     * Before each test, create a new UFundController object and inject
     * a mock UFund DAO
     */
    @BeforeEach
    public void setupNeedController() {
        mockUFundDAO = mock(UFundDAO.class);
        mockBundleDAO = mock(UFundBundleDAO.class);
        needController = new NeedController(mockUFundDAO, mockBundleDAO);
    }

    @Test
    public void testGetNeed() throws IOException {  // getNeed may throw IOException
        // Setup
        Need need = new Need(2, "Food", 4);
        // When the same id is passed in, our mock UFund DAO will return the Need object
        when(mockUFundDAO.getNeed(need.getId())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(need.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 99;
        // When the same id is passed in, our mock Need DAO will return null, simulating
        // no Need found
        when(mockUFundDAO.getNeed(needId)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 5;
        // When getNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockUFundDAO).getNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all NeedController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateNeed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(3,"Seeds",6);
        // when createNeed is called, return true simulating successful
        // creation and save
        when(mockUFundDAO.createNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testCreateNeedFailed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(4,"Blanket",43);
        // when createNeed is called, return false simulating failed
        // creation and save
        when(mockUFundDAO.createNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateNeedHandleException() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(5,"Trash bag",5);

        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockUFundDAO).createNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(6,"broom",7);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockUFundDAO.getNeed(need.getId())).thenReturn(need);
        ResponseEntity<Need> response = needController.updateNeed(need);
        need.setTitle("bread");

        // Invoke
        response = needController.updateNeed(need);
        
        // Analyze
        //assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(7,"wood", 343);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockUFundDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(8,"cookies",45);
        // When updateNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockUFundDAO).getNeed(need.getId());

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testgetNeeds() throws IOException { // getNeeds may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(2, "Food", 4);
        needs[1] = new Need(3,"Seeds",6);

        // When getNeeds is called return the needs created above
        when(mockUFundDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testgetNeedsHandleException() throws IOException { // getNeeds may throw IOException
        // Setup
        // When getNeeds is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockUFundDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testsearchNeeds() throws IOException{ // findNeeds may throw IOException
        // Setup
        String searchString = "Fo";
        Need[] needs = new Need[2];
        needs[0] = new Need(2, "Food", 4);
        needs[1] = new Need(3,"Seeds",6);
        // When findNeeds is called with the search string, return the two
        /// needs above
        when(mockUFundDAO.getNeeds()).thenReturn(needs);

        System.err.println(needController.toString());
        // Invoke
        ResponseEntity<List<Need>> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs[0], response.getBody().get(0)); 
    }

    @Test
    public void testsearchNeedsHandleException() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "an";
        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockUFundDAO).getNeeds();

        // Invoke
        ResponseEntity<List<Need>> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteNeed() throws IOException { // deleteNeed may throw IOException
        // Setup
        Need need = new Need(8,"cookies",45);
        // when deleteNeed is called return true, simulating successful deletion
        when(mockUFundDAO.getNeed(need.getId())).thenReturn(need);
        when(mockUFundDAO.deleteNeed(need.getId())).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(need.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteNeed may throw IOException
        // Setup
        int NeedId = 4;
        // when deleteNeed is called return false, simulating failed deletion
        when(mockUFundDAO.deleteNeed(NeedId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(NeedId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteNeed may throw IOException
        // Setup
        int NeedId = 6;
        // When deleteNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockUFundDAO).getNeed(NeedId);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(NeedId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
