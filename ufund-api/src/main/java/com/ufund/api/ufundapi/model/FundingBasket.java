package com.ufund.api.ufundapi.model;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

//import org.apache.commons.logging.Log;

//import com.ufund.api.ufundapi.controller.NeedController;
import java.util.logging.Level;

public class FundingBasket {

     @JsonProperty("needs") private List<Need> needs;
     
    private static final Logger LOG = Logger.getLogger(FundingBasket.class.getName());

    public FundingBasket(List<Need> needs){
        this.needs = needs;
    }

    public FundingBasket(){
        this.needs = new ArrayList<Need>();
    }

     /**
     * Adds a need to the funding basket.
     * @param need the need to add
     */
    public boolean addNeed(Need need) {
        if (need == null) {
            LOG.log(Level.WARNING, "Attempted to add a null Need to the FundingBasket.");
            return false;
        }
        // Assuming 'needs' does not allow duplicates, we should check if the list already contains the need
        //if (!needs.contains(need)) {
        needs.add(need);
        LOG.log(Level.INFO, "Need added to the FundingBasket: " + need.toString());
        return true;
        //} else {
            //LOG.log(Level.INFO, "Attempted to add a duplicate Need to the FundingBasket: " + need.toString());
            //return false;
        //}
    }

    public boolean removeNeed(Need need) {
        if (need == null) {
            LOG.log(Level.WARNING, "Attempted to remove a null Need from the FundingBasket.");
            return false;
        }
        boolean isRemoved = needs.remove(need);
        if (isRemoved) {
            LOG.log(Level.INFO, "Need removed from the FundingBasket: " + need.toString());
        } else {
            LOG.log(Level.INFO, "Attempted to remove a Need that was not in the FundingBasket: " + need.toString());
        }
        return isRemoved;
    }


    /**
     * get the list of needs in the funding basket
     * @return needs
     */
    public List<Need> listNeeds()
    {
        return needs;
    }
}
