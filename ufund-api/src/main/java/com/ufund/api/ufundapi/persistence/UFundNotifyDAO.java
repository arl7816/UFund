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

import com.ufund.api.ufundapi.model.Notification;
import com.ufund.api.ufundapi.model.User;

/**
 * Implements the functionality for JSON file-based peristance for Notifications
 *
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as notifyed
 */
@Component
public class UFundNotifyDAO implements UFundDAONotify {
    private static final Logger LOG = Logger.getLogger(UFundFileDAO.class.getName());
    Map<Integer,Notification> notifys;   // Provides a local cache of the notify objects

    // so that we don't notify to read from the file
    // each time
    private ObjectMapper objectMapper;  // Provides conversion between Notification
    // objects and JSON text format written
    // to the file
    private static int nextId;  // The next Id to assign to a new notify
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Notification File Data Access Object
     *
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     *
     * @throws IOException when file cannot be accessed or read from
     */
    public UFundNotifyDAO(@Value("${notify.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the notifys from the file
    }

    /**
     * Generates the next id for a new {@linkplain Notification notify}
     *
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Notification notifys} from the tree map
     *
     * @return  The array of {@link Notification notifys}, may be empty
     */
    private Notification[] getNotificationsArray() {
        return getNotificationsArray(null);
    }

    /**
     * Generates an array of {@linkplain Notification notifys} from the tree map for any
     * {@linkplain Notification notifys} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Notification notifys}
     * in the tree map
     *
     * @return  The array of {@link Notification notifys}, may be empty
     */
    private Notification[] getNotificationsArray(String containsText) { // if containsText == null, no filter
        ArrayList<Notification> notifyArrayList = new ArrayList<>();

        for (Notification notify : notifys.values()) {
            if (containsText == null || notify.getContent().contains(containsText)) {
                notifyArrayList.add(notify);
            }
        }

        Notification[] notifyArray = new Notification[notifyArrayList.size()];
        notifyArrayList.toArray(notifyArray);
        return notifyArray;
    }

    /**
     * Saves the {@linkplain Notification notifys} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link Notification notifys} were written successfully
     *
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Notification[] notifyArray = getNotificationsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),notifyArray);
        return true;
    }

    /**
     * Loads {@linkplain Notification notifys} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     *
     * @return true if the file was read successfully
     *
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        notifys = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of notifys
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Notification[] notifyArray = objectMapper.readValue(new File(filename),Notification[].class);

        // Add each notify to the tree map and keep track of the greatest id
        for (Notification notify : notifyArray) {
            notifys.put(notify.getId(),notify);
            if (notify.getId() > nextId)
                nextId = notify.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Notification[] getNotifications() {
        synchronized(notifys) {
            return getNotificationsArray();
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Notification[] findNotifications(String containsText) {
        synchronized(notifys) {
            return getNotificationsArray(containsText);
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Notification getNotification(int id) {
        synchronized(notifys) {
            if (notifys.containsKey(id))
                return notifys.get(id);
            else
                return null;
        }
    }

// /*The following method is purely used in createNotification() to check whether a notify already exsists*/
//     private Notification[] getNotification(String containsText) { // if containsText == null, no filter
//         ArrayList<Notification> notifyArrayList = new ArrayList<>();

//         for (Notification notify : notifys.values()) {
//             if (containsText == null || notify.getTitle().equals(containsText)) {
//                 notifyArrayList.add(notify);
//             }
//         }

//         Notification[] notifyArray = new Notification[notifyArrayList.size()];
//         notifyArrayList.toArray(notifyArray);
//         return notifyArray;
//     }


    /**
     ** {@inheritDoc}
     */
    @Override
    public Notification createNotification(Notification notify) throws IOException {
        synchronized(notifys) {
            // We create a new notify object because the id field is immutable
            // and we notify to assign the next unique id
            
            // Notification[] notifyArray = getNotification(notify.getTitle());
            // if (notifyArray.length>=1){
            //     return null;
            // } else {
            Notification newNotification = new Notification(nextId(),notify.getTitle(), notify.getContent());
            notifys.put(newNotification.getId(),newNotification);
            save(); // may throw an IOException
            return newNotification;
            //}
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Notification updateNotification(Notification notify) throws IOException {
        synchronized(notifys) {
            if (notifys.containsKey(notify.getId()) == false)
                return null;  // notify does not exist

            notifys.put(notify.getId(),notify);
            save(); // may throw an IOException
            return notify;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public boolean deleteNotification(int id) throws IOException {
        synchronized(notifys) {
            if (notifys.containsKey(id)) {
                notifys.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
