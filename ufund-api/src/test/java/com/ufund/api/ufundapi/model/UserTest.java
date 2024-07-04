package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.model.FundingBasket;

import java.io.IOException;
import java.security.InvalidParameterException;


@Tag("Model-Tier")
public class UserTest {
private User[] users;

/*
 * Creates sample array of users to be utilized in testing
 */
@BeforeEach
public void createSample()
{
    users = new User[6];
    users[0] = new User(404,"Robb",null,"password");
    users[1] = new User(1,"Tyrion",null,"password");
    users[2] = new User(27,"Denerys",null,"password");
    users[3] = new User(3,"Melisandre",null,"password");
    users[4] = new User(4,"Tormund",null,"password");
    users[5] = new User(0,"admin",null,"password");
}

/* @Test
public void testConstructor(){
    User user = new User(10, "hi", null);
    assertEquals(null, user.getFundingBasket());


} */

/*
 * Check for correct ID
 */
@Test
public void TestId()
{
    assertEquals(404,users[0].getId());
    assertEquals(1,users[1].getId());
    assertEquals(27,users[2].getId());
    assertEquals(3,users[3].getId());
    assertEquals(4,users[4].getId());
    assertEquals(0,users[5].getId());
}

/*
 * Check for correct names
 */
@Test
public void TestUsername()
{
    assertEquals("Robb",users[0].getUsername());
    assertEquals("Tyrion",users[1].getUsername());
    assertEquals("Denerys",users[2].getUsername());
    assertEquals("Melisandre",users[3].getUsername());
    assertEquals("Tormund",users[4].getUsername());
    assertEquals("admin",users[5].getUsername());

    // checking Username change method
    users[0].setUsername("KingOfTheNorth");
    users[3].setUsername("RedWoman");
        //should NOT change
    users[5].setUsername("Jeoffrey");
    users[1].setUsername("admin");

    assertEquals("KingOfTheNorth",users[0].getUsername());
    assertEquals("RedWoman",users[3].getUsername());
        //should NOT change
    assertEquals("admin",users[5].getUsername());
    assertEquals("Tyrion",users[1].getUsername());
}

/*
 * Check admin assignment
 */
@Test
public void checkAdmin()
{
    assertEquals(false,users[0].isAdmin());
    assertEquals(false,users[1].isAdmin());
    assertEquals(false,users[2].isAdmin());
    assertEquals(false,users[3].isAdmin());
    assertEquals(false,users[4].isAdmin());
    assertEquals(true,users[5].isAdmin());
}


/*
 * check funding baskets
 */
/* 
@Test
public void testFundingBaskets()
{
    assertEquals(null,users[0].getFundingBasket());
    assertEquals(null,users[1].getFundingBasket());
    assertEquals(null,users[2].getFundingBasket());
    assertEquals(null,users[3].getFundingBasket());
    assertEquals(null,users[4].getFundingBasket());
    assertEquals(null,users[5].getFundingBasket());

    //try altering, helper & admin
    FundingBasket bob = new FundingBasket(null);
    FundingBasket dave = new FundingBasket(null);
    users[5].setFundingBasket(bob);
    users[0].setFundingBasket(dave);

    assertEquals(null,users[5].getFundingBasket());
    assertEquals(dave,users[0].getFundingBasket());
}
*/

    
}
