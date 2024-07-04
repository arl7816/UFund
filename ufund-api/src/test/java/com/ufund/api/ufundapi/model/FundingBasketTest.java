package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-Tier")
public class FundingBasketTest {
    private FundingBasket[] baskets;
    private List<Need> needs = new ArrayList<>();

    @BeforeEach
    public void setupNeed() throws IOException{
    needs.add(new Need(0, "Hello world", 1));
    needs.add(new Need(1,"bag",5));
    needs.add(new Need(2,"key",34));
    needs.add(new Need(3,"seed",36));
    }

    @BeforeEach
    public void setupBasket() {
        baskets = new FundingBasket[2];
        baskets[0] = new FundingBasket();
        baskets[1] = new FundingBasket(needs);
    }

    @Test
    public void testConstructor(){
        assertTrue(baskets[0].listNeeds().isEmpty());
        assertEquals(needs, baskets[1].listNeeds());
    }
    
    @Test
    public void listNeedsTest(){
        FundingBasket basket = new FundingBasket();
        basket.addNeed(needs.get(0));
        basket.addNeed(needs.get(1));
        basket.addNeed(needs.get(2));
        basket.addNeed(needs.get(3));
        assertEquals(needs.get(0),basket.listNeeds().get(0));
        assertEquals(needs.get(1),basket.listNeeds().get(1));
        assertEquals(needs.get(2),basket.listNeeds().get(2));
        assertEquals(needs.get(3),basket.listNeeds().get(3));
        assertEquals(needs,basket.listNeeds());
    }

    @Test
    public void addNeedTestFalse(){
        FundingBasket basket = new FundingBasket();
        assertFalse(basket.addNeed(null));
    }

    @Test
    public void addNeedTestTrue(){
        FundingBasket basket = new FundingBasket();
        assertTrue(basket.addNeed(needs.get(0)));
    }

    @Test
    public void removeNeedTest(){
        baskets[1].removeNeed(needs.get(2));
        needs.remove(2);
        assertEquals(needs, baskets[1].listNeeds());
    }
    
    @Test
    public void removeNeedTestFalse(){
        assertFalse(baskets[1].removeNeed(null));
        assertFalse(baskets[1].removeNeed(new Need(5,"non-existent need",5)));
    }

    @Test
    public void removeNeedTestTrue(){
        assertFalse(baskets[1].removeNeed(null));
    }
}
