package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Bundle;


public interface UFundDAOBundle {

    /**
     * Retrieves all {@linkplain Bundle Bundles}
     * @return An array of {@link Bundle Bundle} objects, may be empty
     * @throws IOException if an issue with underlying storage
     */
    Bundle[] getBundles() throws IOException;

    /**
     * Finds all {@linkplain Bundle Bundles} whose name contains the given text
     * @param containsText The text to match against
     * @return An array of {@link Bundle Bundles} whose nemes contains the given text, may be empty
     * @throws IOException if an issue with underlying storage
     */
    Bundle[] findBundles(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Bundle Bundle} with the given id
     * @param id The id of the {@link Bundle Bundle} to get
     * @return a {@link Bundle Bundle} object with the matching id
     * null if no {@link Bundle Bundle} with a matching id is found
     * @throws IOException if an issue with underlying storage
     */
    Bundle getBundle(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Bundle Bundle}
     * @param bundle {@linkplain Bundle Bundle} object to be created and saved
     * The id of the Bundle object is ignored and a new uniqe id is assigned
     * @return new {@link Bundle Bundle} if successful, false otherwise 
     * @throws IOException if an issue with underlying storage
     */
    Bundle createBundle(Bundle bundle) throws IOException;

    /**
     * Updates and saves a {@linkplain Bundle Bundle}
     * @param {@link Bundle Bundle} object to be updated and saved
     * @return updated {@link Bundle Bundle} if successful, null if
     * {@link Bundle Bundle} could not be found
     * @throws IOException if underlying storage cannot be accessed
     */
    Bundle updateBundle(Bundle bundle) throws IOException;

    /**
     * Deletes a {@linkplain Bundle Bundle} with the given id
     * @param id The id of the {@link Bundle Bundle}
     * @return true if the {@link Bundle Bundle} was deleted
     * false if Bundle with the given id does not exist
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteBundle(int id) throws IOException;



    
    
}
