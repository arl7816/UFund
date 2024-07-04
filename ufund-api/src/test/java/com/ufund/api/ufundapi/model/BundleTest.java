package com.ufund.api.ufundapi.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.UFundFileDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Tag("Model-Tier")
public class BundleTest {
    private Bundle bundle;
    private List<Need> needs;

    @Test
    @BeforeEach
    public void testConstructor()
    {
        List<Need> needs1 = new ArrayList<Need>();
        needs1.add(new Need(1,"lightsaber",3));
        needs1.add(new Need(2,"Xwing",5));
        needs1.add(new Need(3,"Death Star",1));
        this.bundle = new Bundle(1,"Jedi Kit",needs1);
        this.needs = needs1;
    }

    @Test
    public void testGetList()
    {
        assertEquals(needs, this.bundle.getList());
    }   

    @Test
    public void testSetList()
    {
        List<Need> needs2 = new ArrayList<Need>();
        needs2.add(new Need(1,"droid",3));
        needs2.add(new Need(2,"blaster",5));
        needs2.add(new Need(3,"Tie-Fighter",1));
        this.bundle.setList(needs2);

        assertEquals(needs2,this.bundle.getList());

    }

    @Test
    public void testGetName()
    {
        assertEquals("Jedi Kit", this.bundle.getName());
    }

    @Test
    public void testSetName()
    {
        this.bundle.setName("Empire Domination Starter Set");
        assertEquals("Empire Domination Starter Set",this.bundle.getName());
    }

    @Test
    public void testGetId()
    {
        assertEquals(1, bundle.getId());
    }

    @Test 
    public void testSync() throws IOException
    {
        ObjectMapper objectmapper = mock(ObjectMapper.class);
        Need[] fakefile = new Need[4];
        Need x = new Need(1,"Hyperdrive",1);
        Need y = new Need(3,"Death Star",1);
        fakefile[0] = x;
        fakefile[1] = y;
        fakefile[2] = new Need(4,"Banjo",1);
        fakefile[3] = new Need(5,"Chewbacca",1);

        
        ArrayList<Need> expected = new ArrayList<>();
        expected.add(x);
        expected.add(y);

        when(objectmapper
            .readValue(new File("file"),Need[].class))
                .thenReturn(fakefile);

        UFundFileDAO dao = new UFundFileDAO("file",objectmapper);

        this.bundle.sync(dao);

        assertEquals(expected, this.bundle.getList());
    }
}
