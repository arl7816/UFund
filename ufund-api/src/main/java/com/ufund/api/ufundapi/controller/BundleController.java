package com.ufund.api.ufundapi.controller;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;

import com.ufund.api.ufundapi.model.Bundle;
import com.ufund.api.ufundapi.persistence.UFundDAOBundle;

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
@RequestMapping("bundles")
public class BundleController {

    private UFundDAOBundle dao;
        private static final Logger LOG = Logger.getLogger(BundleController.class.getName());
        public BundleController(UFundDAOBundle dao){
            this.dao = dao;
        }



        /**
         * Searches for Bundles based on a given name (title).
         * If the name is null or empty, all bundles are returned.
         * Otherwise, it returns a list of bundles that contain the given name in their title, case-insensitive.
         * 
         * @param name The search term to filter the bundle titles.
         * @return A ResponseEntity containing the list of filtered bundles and the HTTP status code.
         * @throws IOException If there is an issue with underlying storage during the retrieval of bundles.
         */
        @GetMapping("/")
        public ResponseEntity<List<Bundle>> searchBundles(String name) throws IOException {
            try{
            Bundle[] bundleArray = dao.getBundles(); // Get the array of Bundles
            List<Bundle> allBundles = Arrays.asList(bundleArray); // Convert array to list
    
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.ok(allBundles);
            }
    
            List<Bundle> filteredBundles = allBundles.stream()
                    .filter(bundle -> bundle.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
    
            return ResponseEntity.ok(filteredBundles);
        }
            catch(IOException e) {
                LOG.log(Level.SEVERE,e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }




        /**
     * Responds to the GET request for a {@linkplain Bundle Bundle} for the given id
     * 
     * @param id The id used to locate the {@link Bundle Bundle}
     * 
     * @return ResponseEntity with {@link Bundle Bundle} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    
    @GetMapping("/{id}")
    public ResponseEntity<Bundle> getBundle(@PathVariable int id) {
        /* DONE */
        LOG.info("GET /bundles/" + id);
        try {
        Bundle bundle = this.dao.getBundle(id);
        if (bundle != null)
            return new ResponseEntity<Bundle>(bundle,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
                LOG.log(Level.SEVERE,e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Bundle> deleteBundle(@PathVariable int id) {
        LOG.info("DELETE /bundles/" + id);
        try
        {
            Bundle h = this.dao.getBundle(id);
            if(h!=null)
            {
                this.dao.deleteBundle(id);
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
    public ResponseEntity<Bundle> createBundle(@RequestBody Bundle bundle) {
        LOG.info("POST /bundles " + bundle);
        try {
            Bundle newBundle = dao.createBundle(bundle);
            if (newBundle != null)
                return new ResponseEntity<Bundle>(newBundle, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @PutMapping("")
    public ResponseEntity<Bundle> updateBundle(@RequestBody Bundle bundle) {
        LOG.info("PUT /bundles " + bundle);

        try{
            Bundle foundBundle = this.dao.getBundle(bundle.getId());
            if (foundBundle != null){
                if (bundle.getName() != null){
                    foundBundle.setName(bundle.getName());
                }
                if (bundle.getList()!=null&&!bundle.getList().isEmpty()){
                    foundBundle.setList(bundle.getList());
                }

                this.dao.updateBundle(foundBundle);
                return new ResponseEntity<>(foundBundle, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    

    @GetMapping("")
    public ResponseEntity<Bundle[]> getBundles() {
        LOG.info("GET /bundles");
        
        // Replace below with your implementation
        try {
            Bundle [] bundle = dao.getBundles();
            if (bundle != null)
                return new ResponseEntity<Bundle[]>(bundle,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
  
    
}
