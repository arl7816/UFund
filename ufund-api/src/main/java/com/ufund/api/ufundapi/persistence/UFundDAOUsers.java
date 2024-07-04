package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.User;

public interface UFundDAOUsers {
    /**
     * Gets a specific {@linkplain User user} using an id
     * @param id the id for the user
     * @return the {@linkplain User user}
     * @throws IOException when file can't be accessed and or written to
     */
    public User getUser(int id) throws IOException;
    
    /**
     * Creates a new {@linkplain User user}
     * @param user the new user to be added
     * @return the newly created user
     * @throws IOException when file cant be accessed and or written to
     */
    public User createUser(User user) throws IOException;

    /**
     * Updates and saves a {@linkplain User user}
     *
     * @param {@link User user} object to be updated and saved
     *
     * @return updated {@link User user} if successful, null if
     * {@link User user} could not be found
     *
     * @throws IOException if underlying storage cannot be accessed
     */
    public User updateUser(User user) throws IOException;

    /**
     * Deletes a {@linkplain User user} with the given id
     *
     * @param id The id of the {@link User user}
     *
     * @return true if the {@link User user} was deleted
     * <br>
     * false if need with the given id does not exist
     *
     * @throws IOException if underlying storage cannot be accessed
     */
    public boolean deleteUser(int id) throws IOException;

    /**
     * TODO: still needs to be fully realized
     * @param user
     * @return
     * @throws IOException
     */
    public boolean checkout(User user) throws IOException;

    /**
     * Retrieves all {@linkplain User users}
     *
     * @return An array of {@link User user} objects, may be empty
     *
     * @throws IOException if an issue with underlying storage
     */
    public User[] getUsers() throws IOException;

    /**
     * Finds all {@linkplain User users} whose name contains the given text
     *
     * @param containsText The text to match against
     *
     * @return An array of {@link User users} whose nemes contains the given text, may be empty
     *
     * @throws IOException if an issue with underlying storage
     */
    public User[] findUsers(String containsText) throws IOException;
}
