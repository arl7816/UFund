package com.ufund.api.ufundapi.model;

import java.security.InvalidParameterException;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
     @JsonProperty("id") private final int id;
     @JsonProperty("username") private String username;
     @JsonProperty("password") private String password;

     @JsonProperty("fundingBasket") private FundingBasket fundingBasket;
     @JsonProperty("amAdmin") private final boolean amAdmin;
     @JsonProperty("wallet") private int wallet;
     private final String ADMIN = "admin";

     /**
      * The constructor for a specific user
      * @param id the id for the user, must be unqiue
      * @param name the username for the user, must be unqiue
      * @param fundingBasket the funding basket the user uses to donate to and from needs
      */
     public User(@JsonProperty("id") int id, @JsonProperty("username") String name, 
        @JsonProperty("fundingBasket") FundingBasket fundingBasket, @JsonProperty("password") String password){
            this.id = id;
            this.username = name;
            this.wallet = 0;
            if (fundingBasket==null)
            {
               this.fundingBasket = new FundingBasket();
            }
            else
            {
               this.fundingBasket = fundingBasket;
            }
            
            this.password = password;

            this.amAdmin = this.username == ADMIN;
     }

     /**
      * Gets the id of the current user
      * @return the users id
      */
     public int getId(){ return this.id; }

     /**
      * Gets the users username
      * @return the users username
      */
     public String getUsername(){ return this.username; } 

     /**
      * Sets the user's username, unless the Username is admin
      */
      public void setUsername(String newName){
          if(!(this.username.equals(ADMIN))&&!(newName.equals(ADMIN)))
          this.username = newName;
     }
     /**
      * Gets the users funding basket
      * @return the users funding basket
      */
     public FundingBasket getFundingBasket(){ return this.fundingBasket; }

     /**
      * Sets the user's fundingbasket to a new fundingbasket
      */
      public void setFundingBasket(FundingBasket basket){
          if(!this.amAdmin)
          {
               this.fundingBasket = basket;
          }
      }
  
      /**
      * Determines if a {@linkplain User user} is an admin or not
      * @return true if admin, false otherwise
      */
     public boolean isAdmin(){ return this.amAdmin; }
     
     public int getWallet(){
          return this.wallet;
     }

     public void setWallet(int wallet){
          this.wallet = wallet;
     }

     public String getPassword() {
          return password;
     }

     public void setPassword(String password) {
          this.password = password;
     }

}
