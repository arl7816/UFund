package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Need {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    static final String STRING_FORMAT = "Need [id=%d, name=%s]";
    @JsonProperty("name") private String name;
    @JsonProperty("id") private int id;
    @JsonProperty("goalMet") private boolean metGoal;
    @JsonProperty("cost") private int cost;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private String type;
    @JsonProperty("surplus") private int surplus;


    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("quantity") int quantity) throws ArithmeticException{
        this.id = id;
        this.name = name;
        this.metGoal = false;
        this.setCost(1);
        this.setQuantity(quantity);
        this.type = "Base-Need";
        this.surplus = 0;
    }

    public boolean hasMetGoal(){
        return this.metGoal;
    }

    public void setGoalMet(@JsonProperty("goalMet") boolean hasMetGoal){
        this.metGoal = hasMetGoal;
    }

    public void setCost(@JsonProperty("cost") int cost) throws ArithmeticException{
        if (cost <= 0){
            throw new ArithmeticException("Cost must be greater than 0");
        }
        this.cost = cost;
    }

    public void setQuantity(@JsonProperty("quantity") int quantity) throws ArithmeticException{
        if (quantity < 0){
            throw new ArithmeticException("Quantity can't be less than 0");
        }
        this.quantity = quantity;
    }

    public void setType(@JsonProperty("type") String type){
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.name;
    }

    public void setTitle(String title){
        this.name = title;
    }

    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name);
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getType(){
        return this.type;
    }

    public int getCost(){
        return this.cost;
    }

    public int getSurplus() {
        return surplus;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    /*
     * For use in Junit testing
     */
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Need)
        {
            Need n = (Need) other;
            if(n.getId()==this.id)
            {
                return true;
            }

        }
        return false;
    }
}
