package com.ufund.api.ufundapi.controller;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;

import com.ufund.api.ufundapi.model.Bundle;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.UFundDAO;
import com.ufund.api.ufundapi.persistence.UFundDAOBundle;
import com.ufund.api.ufundapi.persistence.UFundFileDAO;

import java.io.IOException;
import java.util.Arrays;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("ufund")
public class NeedController {
    
        private UFundDAO dao; // Assume this is an instance of UFundFileDAO
        private UFundDAOBundle dao2;
        private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
        public NeedController(UFundDAO dao, UFundDAOBundle dao2){
            this.dao = dao;
            this.dao2 = dao2;
        }
    
        /**
         * Searches for needs based on a given name (title).
         * If the name is null or empty, all needs are returned.
         * Otherwise, it returns a list of needs that contain the given name in their title, case-insensitive.
         * 
         * @param name The search term to filter the need titles.
         * @return A ResponseEntity containing the list of filtered needs and the HTTP status code.
         * @throws IOException If there is an issue with underlying storage during the retrieval of needs.
         */
        @GetMapping("/")
        public ResponseEntity<List<Need>> searchNeeds(String name) throws IOException {
            try{
            Need[] needsArray = dao.getNeeds(); // Get the array of Needs
            List<Need> allNeeds = Arrays.asList(needsArray); // Convert array to list
    
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.ok(allNeeds);
            }
    
            List<Need> filteredNeeds = allNeeds.stream()
                    .filter(need -> need.getTitle().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
    
            return ResponseEntity.ok(filteredNeeds);
        }
            catch(IOException e) {
                LOG.log(Level.SEVERE,e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
  
    /**
     * Responds to the GET request for a {@linkplain Need need} for the given id
     * 
     * @param id The id used to locate the {@link Need need}
     * 
     * @return ResponseEntity with {@link Need need} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    
    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id) {
        /* DONE */
        LOG.info("GET /needs/" + id);
        try {
        Need need = this.dao.getNeed(id);
        if (need != null)
            return new ResponseEntity<Need>(need,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
                LOG.log(Level.SEVERE,e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id) {
        LOG.info("DELETE /needs/" + id);
        try
        {
            Need h = this.dao.getNeed(id);
            if(h!=null)
            {
                this.dao.deleteNeed(id); // for bundle support
                Bundle[] temp = this.dao2.getBundles();
                if (temp != null){
                    for(Bundle b: temp)
                    {
                        b.sync((UFundFileDAO)this.dao);
                    }
                }     
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need) {
        LOG.info("POST /ufund " + need);
        try {
            Need newNeed = dao.createNeed(need);
            if (newNeed != null)
                return new ResponseEntity<Need>(newNeed, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need) {
        LOG.info("PUT /needs " + need);

        try{
            Need foundNeed = this.dao.getNeed(need.getId());
            if (foundNeed != null){
                if (need.getTitle() != null){
                    foundNeed.setTitle(need.getTitle());
                }
                if (need.getType() != null){
                    foundNeed.setType(need.getType());
                }
                if (need.getCost() != 0){
                    foundNeed.setCost(need.getCost());
                }
                if (need.getQuantity() != -1){
                    foundNeed.setQuantity(need.getQuantity());
                }

                if (need.hasMetGoal()){
                    foundNeed.setGoalMet(true);
                }

                if (need.getSurplus() != 0){
                    foundNeed.setSurplus(need.getSurplus());
                }

                this.dao.updateNeed(foundNeed);
                System.out.println("Found need at " + foundNeed);
                Bundle[] temp = this.dao2.getBundles(); //for bundle support
                if (temp != null){
                    for(Bundle b: temp)
                    {
                        b.sync((UFundFileDAO)this.dao);
                    }
                }
                return new ResponseEntity<>(foundNeed, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    


    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds() {
        LOG.info("GET /needs");
        
        // Replace below with your implementation
        try {
            Need [] Need = dao.getNeeds();
            if (Need != null)
                return new ResponseEntity<Need[]>(Need,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
