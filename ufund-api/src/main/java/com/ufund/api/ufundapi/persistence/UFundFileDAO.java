package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

/**
 * Implements the functionality for JSON file-based peristance for Needs
 *
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 */
@Component
public class UFundFileDAO implements UFundDAO {
    private static final Logger LOG = Logger.getLogger(UFundFileDAO.class.getName());
    Map<Integer,Need> needs;   // Provides a local cache of the hero objects

    // so that we don't need to read from the file
    // each time
    private ObjectMapper objectMapper;  // Provides conversion between Need
    // objects and JSON text format written
    // to the file
    private static int nextId;  // The next Id to assign to a new hero
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Need File Data Access Object
     *
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     *
     * @throws IOException when file cannot be accessed or read from
     */
    public UFundFileDAO(@Value("${needs.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the needs from the file
    }

    /**
     * Generates the next id for a new {@linkplain Need hero}
     *
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Need needs} from the tree map
     *
     * @return  The array of {@link Need needs}, may be empty
     */
    private Need[] getNeedsArray() {
        return getNeedsArray(null);
    }

    /**
     * Generates an array of {@linkplain Need needs} from the tree map for any
     * {@linkplain Need needs} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Need needs}
     * in the tree map
     *
     * @return  The array of {@link Need needs}, may be empty
     */
    private Need[] getNeedsArray(String containsText) { // if containsText == null, no filter
        ArrayList<Need> heroArrayList = new ArrayList<>();

        for (Need hero : needs.values()) {
            if (containsText == null || hero.getTitle().contains(containsText)) {
                heroArrayList.add(hero);
            }
        }

        Need[] heroArray = new Need[heroArrayList.size()];
        heroArrayList.toArray(heroArray);
        return heroArray;
    }

    /**
     * Saves the {@linkplain Need needs} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link Need needs} were written successfully
     *
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Need[] heroArray = getNeedsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),heroArray);
        return true;
    }

    /**
     * Loads {@linkplain Need needs} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     *
     * @return true if the file was read successfully
     *
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        needs = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of needs
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Need[] heroArray = objectMapper.readValue(new File(filename),Need[].class);

        // Add each hero to the tree map and keep track of the greatest id
        for (Need hero : heroArray) {
            needs.put(hero.getId(),hero);
            if (hero.getId() > nextId)
                nextId = hero.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need[] getNeeds() {
        synchronized(needs) {
            return getNeedsArray();
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need[] findNeeds(String containsText) {
        synchronized(needs) {
            return getNeedsArray(containsText);
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need getNeed(int id) {
        synchronized(needs) {
            if (needs.containsKey(id))
                return needs.get(id);
            else
                return null;
        }
    }

/*The following method is purely used in createNeed() to check whether a need already exsists*/
    private Need[] getNeed(String containsText) { // if containsText == null, no filter
        ArrayList<Need> heroArrayList = new ArrayList<>();

        for (Need hero : needs.values()) {
            if (containsText == null || hero.getTitle().equals(containsText)) {
                heroArrayList.add(hero);
            }
        }

        Need[] heroArray = new Need[heroArrayList.size()];
        heroArrayList.toArray(heroArray);
        return heroArray;
    }


    /**
     ** {@inheritDoc}
     */
    @Override
    public Need createNeed(Need need) throws IOException {
        synchronized(needs) {
            // We create a new hero object because the id field is immutable
            // and we need to assign the next unique id
            Need[] needArray = getNeed(need.getTitle());
            if (needArray.length>=1){
                return null;
            } else {
            Need newNeed = new Need(nextId(),need.getTitle(), need.getQuantity());
            needs.put(newNeed.getId(),newNeed);
            save(); // may throw an IOException
            return newNeed;
            }
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need updateNeed(Need hero) throws IOException {
        synchronized(needs) {
            if (needs.containsKey(hero.getId()) == false)
                return null;  // hero does not exist

            needs.put(hero.getId(),hero);
            save(); // may throw an IOException
            return hero;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public boolean deleteNeed(int id) throws IOException {
        synchronized(needs) {
            if (needs.containsKey(id)) {
                needs.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
