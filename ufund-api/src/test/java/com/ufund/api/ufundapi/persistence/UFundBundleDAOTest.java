package com.ufund.api.ufundapi.persistence;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Bundle;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UFundBundleDAO;

import java.io.*;
import java.util.ArrayList;



@Tag("Model-Tier")
public class UFundBundleDAOTest {
    private UFundBundleDAO dao;
    
    private ObjectMapper mapper = mock(ObjectMapper.class);
    
    private Bundle[] bundles;
    private Need[] needs;
    
    @BeforeEach
    public void init() throws IOException
    {
        Need a = new Need(1,"Lightsaber",5);
        Need b = new Need(2, "Robes", 1);
        Need c = new Need(3, "Wolfsbane", 33);
        Need d = new Need(4, "Tatooine", 1);
        Need e = new Need(5, "X-wing", 10);
        Need f = new Need(6,"Dilithium", 5);
        Need g = new Need(7,"platinum", 44);
        this.needs = new Need[7];
        this.needs[0] = a ;
        this.needs[1] = b ;
        this.needs[2] = c ;
        this.needs[3] = d ;
        this.needs[4] = e ;
        this.needs[5] = f ;
        this.needs[6] = g ;
        ArrayList<Need> x = new ArrayList<>();
        x.add(a);
        x.add(b);
        x.add(d);
        x.add(e);
        ArrayList<Need> y = new ArrayList<>();
        y.add(b);
        y.add(c);
        y.add(g);
        ArrayList<Need> z = new ArrayList<>();
        z.add(f);
        z.add(g);
        z.add(e);
        z.add(d);
        z.add(a);
        this.bundles = new Bundle[3];
        this.bundles[0] = new Bundle(1,"Jedi-Kit",x);
        this.bundles[1] = new Bundle(2,"Wizarding Basics",y);
        this.bundles[2] = new Bundle(3,"Space Exploration Pack",z);

        when(mapper
            .readValue(new File("temp"),Bundle[].class))
                .thenReturn(this.bundles);

        
                
        this.dao = new UFundBundleDAO("temp", mapper);
        
        
    }

    @Test
    public void testCreateBundle() throws IOException
    {
        Need e = new Need(5, "X-wing", 10);
        Need f = new Need(6,"Dilithium", 5);
        Need g = new Need(7,"platinum", 44);
        ArrayList<Need> temp = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle test = new Bundle(4,"Build A SpaceShip!",temp);
        // create bundle
        dao.createBundle(test);
        //retrieve
        assertEquals(test,dao.getBundle(4));
        assertEquals(test.getName(),dao.getBundle(4).getName());
        assertEquals(test.getList(),dao.getBundle(4).getList());

        // cannot re-create
        assertEquals(null,dao.createBundle(test));

    }


    @Test
    public void testGetBundles()
    {
        assertEquals(this.bundles.length,dao.getBundles().length);
        Bundle[] temp = dao.getBundles();
        for(int i = 0; i<this.bundles.length; i++)
        {
            assertEquals(this.bundles[i],temp[i]);
        }
    }


    @Test
    public void testSearchBundles()
    {
        Bundle[] temp = dao.findBundles("Basics");
        assertEquals(1,temp.length);
        assertEquals(this.bundles[1],temp[0]);

        Bundle[] testempty = dao.findBundles("sowebnvm[fnm,v]");
        assertEquals(0,testempty.length);
    }

    @Test
    public void testGetBundle()
    {
        Bundle x = dao.getBundle(1);
        assertEquals(this.bundles[0],x);
        assertNull(dao.getBundle(1234567890));
    }

    @Test
    public void testUpdateBundle() throws IOException
    {

        Need e = new Need(5, "X-wing", 10);
        Need f = new Need(6,"Dilithium", 5);
        Need g = new Need(7,"platinum", 44);
        ArrayList<Need> temp = new ArrayList<>();
        temp.add(e);
        temp.add(f);
        temp.add(g);

        Bundle x = new Bundle (45,"Festival Bundle",temp);
        assertNull(dao.updateBundle(x));
        x = new Bundle (1,"Festival Bundle",temp);
        assertEquals(x, dao.updateBundle(x));
    }


    @Test
    public void testDeleteBundle() throws IOException
    {
        assertTrue(dao.deleteBundle(3));
        assertNull(dao.getBundle(3));

        assertFalse(dao.deleteBundle(27));
    }








}
