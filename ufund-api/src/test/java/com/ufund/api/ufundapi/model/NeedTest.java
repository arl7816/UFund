package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Need;

import java.io.IOException;


@Tag("Model-Tier")
class NeedTest{
    private Need[] needs;

    @BeforeEach
    public void setupNeed() throws IOException{
        needs = new Need[5];
        needs[0] = new Need(0, "Hello world", 1);
        needs[1] = new Need(1, null, 1);
        needs[2] = new Need(-2, "Thats me", 4);
        needs[3] = new Need(3, "thats me", 20);
        needs[4] = new Need(5, "hi", 17);
    }

    @Test
    public void testConstructor(){
        assertThrows(ArithmeticException.class, () -> new Need(0, null, -1));
    }

	@Test
    public void testId(){
        assertEquals(0, needs[0].getId());
        assertEquals(1, needs[1].getId());
        assertEquals(-2, needs[2].getId());
        assertEquals(3, needs[3].getId());
        assertEquals(5, needs[4].getId());
    }

    @Test
    public void testTitle(){
        assertEquals("Hello world", needs[0].getTitle());
        assertEquals(null, needs[1].getTitle());
        assertEquals("Thats me", needs[2].getTitle());
        assertEquals("thats me", needs[3].getTitle());
        assertEquals("hi", needs[4].getTitle());

        needs[0].setTitle("new test");
        assertEquals("new test", needs[0].getTitle());
    }

    @Test
    public void testQuantity(){
        assertEquals(1, needs[0].getQuantity());
        assertEquals(1, needs[1].getQuantity());
        assertEquals(4, needs[2].getQuantity());
        assertEquals(20, needs[3].getQuantity());
        assertEquals(17, needs[4].getQuantity());

        needs[0].setQuantity(1000);
        assertEquals(1000, needs[0].getQuantity());

        assertThrows(ArithmeticException.class, () -> needs[0].setQuantity(-1));
    }

    @Test
    public void testType(){
        for (Need need: needs){
            assertEquals("Base-Need", need.getType());
        }

        needs[0].setType(null);
        needs[1].setType("testing type");

        assertEquals(null, needs[0].getType());
        assertEquals("testing type", needs[1].getType());
    }

    @Test
    public void testGoalMet(){
        for (Need need: needs){
            assertFalse(need.hasMetGoal());
        }

        for (Need need: needs){
            need.setGoalMet(true);
            assertTrue(need.hasMetGoal());
        }

    }

    @Test
    public void testCost(){
        for (Need need: needs){
            assertEquals(1, need.getCost());
        }

        needs[0].setCost(5);
        needs[1].setCost(2);

        assertEquals(5, needs[0].getCost());
        assertEquals(2, needs[1].getCost());

        assertThrows(ArithmeticException.class, () -> needs[0].setCost(0));
        assertThrows(ArithmeticException.class, () -> needs[0].setCost(-1));
    }

    @Test
    public void testToString(){
        assertEquals("Need [id=0, name=Hello world]", needs[0].toString());
        assertEquals("Need [id=1, name=null]", needs[1].toString());
        assertEquals("Need [id=-2, name=Thats me]", needs[2].toString());
        assertEquals("Need [id=3, name=thats me]", needs[3].toString());
        assertEquals("Need [id=5, name=hi]", needs[4].toString());
    }

    @Test
    public void testEquals(){
        Need need1 = new Need(1, "Test1", 0);
        Need need2 = new Need(1, "not the same name", 20);
        Need need3 = new Need(10, "Test1", 0);

        Need need4 = need1;
        Need need5 = new Need(1, "Test1", 0);

        Object obj = new Object();

        assertTrue(need1.equals(need2));
        assertFalse(need1.equals(need3));
        assertTrue(need1.equals(need4));
        assertTrue(need1.equals(need5));
        assertFalse(need1.equals(obj));


    }

}