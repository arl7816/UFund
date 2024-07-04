package com.ufund.api.ufundapi.persistence;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Bundle;

@Component
public class UFundBundleDAO implements UFundDAOBundle{

   private static final Logger LOG = Logger.getLogger(UFundBundleDAO.class.getName());
    Map<Integer,Bundle> bundles;   // Provides a local cache of the hero objects

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
    public UFundBundleDAO(@Value("${bundle.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the Bundles from the file
    }


    /**
     * Generates the next id for a new {@linkplain Bundle hero}
     *
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Bundle bundles} from the tree map
     *
     * @return  The array of {@link Bundle bundles}, may be empty
     */
    private Bundle[] getBundleArray() {
        return getBundleArray(null);
    }

    /**
     * Generates an array of {@linkplain Bundle Bundles} from the tree map for any
     * {@linkplain Bundle Bundles} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Bundle Bundles}
     * in the tree map
     *
     * @return  The array of {@link Bundle Bundles}, may be empty
     */
    private Bundle[] getBundleArray(String containsText) { // if containsText == null, no filter
        ArrayList<Bundle> heroArrayList = new ArrayList<>();

        for (Bundle hero : bundles.values()) {
            if (containsText == null || hero.getName().contains(containsText)) {
                heroArrayList.add(hero);
            }
        }

        Bundle[] heroArray = new Bundle[heroArrayList.size()];
        heroArrayList.toArray(heroArray);
        return heroArray;
    }

    /**
     * Saves the {@linkplain Bundle Bundles} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link Bundle Bundles} were written successfully
     *
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Bundle[] heroArray = getBundleArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),heroArray);
        return true;
    }

    /**
     * Loads {@linkplain Bundle Bundles} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     *
     * @return true if the file was read successfully
     *
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        bundles = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of Bundles
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Bundle[] heroArray = objectMapper.readValue(new File(filename),Bundle[].class);

        // Add each hero to the tree map and keep track of the greatest id
        for (Bundle hero : heroArray) {
            bundles.put(hero.getId(),hero);
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
    public Bundle[] getBundles() {
        synchronized(bundles) {
            return getBundleArray();
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Bundle[] findBundles(String containsText) {
        synchronized(bundles) {
            return getBundleArray(containsText);
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Bundle getBundle(int id) {
        synchronized(bundles) {
            if (bundles.containsKey(id))
                return bundles.get(id);
            else
                return null;
        }
    }

/*The following method is purely used in createBundle() to check whether a Bundle already exsists*/
    private Bundle[] getBundle(String containsText) { // if containsText == null, no filter
        ArrayList<Bundle> heroArrayList = new ArrayList<>();

        for (Bundle hero : bundles.values()) {
            if (containsText == null || hero.getName().equals(containsText)) {
                heroArrayList.add(hero);
            }
        }

        Bundle[] heroArray = new Bundle[heroArrayList.size()];
        heroArrayList.toArray(heroArray);
        return heroArray;
    }


    /**
     ** {@inheritDoc}
     */
    @Override
    public Bundle createBundle(Bundle Bundle) throws IOException {
        synchronized(bundles) {
            // We create a new hero object because the id field is immutable
            // and we Bundle to assign the next unique id
            Bundle[] BundleArray = getBundle(Bundle.getName());
            if (BundleArray.length>=1){
                return null;
            } else {
            Bundle newBundle = new Bundle(nextId(),Bundle.getName(), Bundle.getList());
            bundles.put(newBundle.getId(),newBundle);
            save(); // may throw an IOException
            return newBundle;
            }
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Bundle updateBundle(Bundle hero) throws IOException {
        synchronized(bundles) {
            if (bundles.containsKey(hero.getId()) == false)
                return null;  // hero does not exist

            bundles.put(hero.getId(),hero);
            save(); // may throw an IOException
            return hero;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public boolean deleteBundle(int id) throws IOException {
        synchronized(bundles) {
            if (bundles.containsKey(id)) {
                bundles.remove(id);
                return save();
            }
            else
                return false;
        }
    }
    
    
    
}
