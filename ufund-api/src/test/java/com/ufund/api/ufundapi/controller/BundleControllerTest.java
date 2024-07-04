package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Bundle;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.UFundBundleDAO;


@Tag("Controller-Tier")
public class BundleControllerTest {
    private UFundBundleDAO dao;
    private BundleController cont;


    @BeforeEach
    public void init()
    {
        dao = mock(UFundBundleDAO.class);
        cont = new BundleController(dao);
    }


    @Test
    public void testGet() throws IOException
    {
        Need e = new Need(5, "X-wing", 10);
        Need f = new Need(6,"Dilithium", 5);
        Need g = new Need(7,"platinum", 44);
        ArrayList<Need> temp = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test = new Bundle(4,"Build A SpaceShip!",temp);
        when(dao.getBundle(test.getId())).thenReturn(test);

        ResponseEntity<Bundle> x = cont.getBundle(test.getId());

        assertEquals(HttpStatus.OK, x.getStatusCode());
        assertEquals(test, x.getBody());

        when(dao.getBundle(23456)).thenReturn(null);
        x = cont.getBundle(23456);

        assertEquals(HttpStatus.NOT_FOUND, x.getStatusCode());

        //doThrow(new IOException()).when(this.dao).getBundle(0);
        //x = cont.getBundle(0);

        //assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,x.getStatusCode());
    }


    @Test
    public void testGetAll()
    {
        Need e = new Need(5, "X-wing", 10);
        Need f = new Need(6,"Dilithium", 5);
        Need g = new Need(7,"platinum", 44);
        ArrayList<Need> temp = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test = new Bundle(1,"Build A Space!",temp);
       
        ArrayList<Need> temp2 = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test2 = new Bundle(2,"Build A Ship!",temp2);
 
        ArrayList<Need> temp3 = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test3 = new Bundle(3,"A SpaceShip!",temp3);

        Bundle[] x = new Bundle[3];
        x[0] = test;
        x[1] = test2;
        x[2] = test3;

        when(dao.getBundles()).thenReturn(x);

        ResponseEntity<Bundle[]> response = cont.getBundles();
        assertEquals(x,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());

        for (int i = 0; i < x.length; i++) {
            assertEquals(x[i], response.getBody()[i]);
        }

        when(dao.getBundles()).thenReturn(null);
        
        assertEquals(HttpStatus.NOT_FOUND,cont.getBundles().getStatusCode());


    }



    @Test
    public void testcreate() throws IOException
    {
        Need e = new Need(5, "X-wing", 10);
        Need f = new Need(6,"Dilithium", 5);
        Need g = new Need(7,"platinum", 44);
        ArrayList<Need> temp = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test = new Bundle(1,"Build A Space!",temp);

        when(dao.createBundle(test)).thenReturn(test);
        ResponseEntity<Bundle> response = cont.createBundle(test) ;

        assertEquals(test, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(dao.createBundle(test)).thenReturn(null);
        response = cont.createBundle(test);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        when(dao.createBundle(null)).thenThrow(IOException.class);
        response = cont.createBundle(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        
    }


    @Test
    public void testSearch() throws IOException
    {
        Need e = new Need(5, "X-wing", 10);
        Need f = new Need(6,"Dilithium", 5);
        Need g = new Need(7,"platinum", 44);
        ArrayList<Need> temp = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test = new Bundle(1,"Build A Space!",temp);
       
        ArrayList<Need> temp2 = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test2 = new Bundle(2,"Build A Ship!",temp2);
 
        ArrayList<Need> temp3 = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test3 = new Bundle(3,"A SpaceShip!",temp3);

        Bundle[] x = new Bundle[3];
        x[0] = test;
        x[1] = test2;
        x[2] = test3;

        List<Bundle> y = new ArrayList<>();
        y.add(test);
        y.add(test2);
        y.add(test3);

        when(dao.getBundles()).thenReturn(x);

        ResponseEntity<List<Bundle>> response = cont.searchBundles("A");
        assertEquals(y,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());

        for (int i = 0; i < x.length; i++) {
            assertEquals(x[i], response.getBody().get(i));
        }

        when(dao.getBundles()).thenReturn(null);
        
        assertEquals(HttpStatus.NOT_FOUND,cont.getBundles().getStatusCode());

    }


    @Test
    public void testDelete() throws IOException
    {
        Need e = new Need(5, "X-wing", 10);
        Need f = new Need(6,"Dilithium", 5);
        Need g = new Need(7,"platinum", 44);
        ArrayList<Need> temp = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test = new Bundle(1,"Build A Space!",temp);

        when(dao.getBundle(1)).thenReturn(null);
        ResponseEntity<Bundle> response = cont.deleteBundle(1);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

        when(dao.getBundle(2)).thenReturn(test);
        when(dao.deleteBundle(2)).thenReturn(true);
        response = cont.deleteBundle(2);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        
        when(dao.getBundle(0)).thenReturn(test);
        doThrow(new IOException()).when(dao).deleteBundle(0);
        response = cont.deleteBundle(0);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        
        
    }



    @Test
    public void testUpdate() throws IOException
    {

        Need e = new Need(5, "X-wing", 10);
        Need f = new Need(6,"Dilithium", 5);
        Need g = new Need(7,"platinum", 44);
        ArrayList<Need> temp = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test = new Bundle(1,"Build A Space!",temp);

        when(dao.getBundle(1)).thenReturn( null);
        ResponseEntity<Bundle> x = cont.updateBundle(test);

        assertEquals(HttpStatus.NOT_FOUND,x.getStatusCode());

        when(dao.getBundle(1)).thenReturn(test);
        when(dao.updateBundle(test)).thenReturn(test);
        x = cont.updateBundle(test);

        assertEquals(HttpStatus.OK,x.getStatusCode());
        assertEquals(test,x.getBody());


        when(dao.getBundle(1)).thenReturn(test);
        when(dao.updateBundle(test)).thenThrow(IOException.class);
        x = cont.updateBundle(test);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,x.getStatusCode());



    }

    
}
