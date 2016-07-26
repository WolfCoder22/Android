package edu.dartmouth.cs.alexwolf.myruns.backend.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexwolf on 2/24/16.
 */
public class EntryDatastore {

    private static final Logger mLogger = Logger
            .getLogger(EntryDatastore.class.getName());

    private static final DatastoreService mDatastore = DatastoreServiceFactory
            .getDatastoreService();

    private static Key getKey() {
        return KeyFactory.createKey(ExerciseEntry.EXERCISE_ENTRY_PARENT_ENTITY_NAME,
                ExerciseEntry.EXERCISE_ENTRY_PARENT_KEY_NAME);
    }

    //add entry to database if it isn't already there
    public static boolean add(ExerciseEntry entry) {
        if (getEntryByName(entry.mId, null) != null) {
            mLogger.log(Level.INFO, "Entry exists");
            return false;
        }

        Key parentKey = getKey();

        //create new entity with exercise entry data
        Entity entity = new Entity(ExerciseEntry.ENTRY_ENTITY_NAME, entry.mId,
                parentKey);
        entity.setProperty(ExerciseEntry.FIELD_ID_NAME, entry.mId);
        entity.setProperty(ExerciseEntry.FIELD_NAME_ACTIVITY, entry.mActivityType);
        entity.setProperty(ExerciseEntry.FIELD_NAME_DURATION, entry.mDuration);
        entity.setProperty(ExerciseEntry.FIELD_NAME_DISTANCE, entry.mDistance);
        entity.setProperty(ExerciseEntry.FIELD_NAME_DATE_TIME, entry.mDateTime);
        entity.setProperty(ExerciseEntry.FIELD_NAME_CALORIES, entry.mCalorie);
        entity.setProperty(ExerciseEntry.FIELD_NAME_CLIMB, entry.mClimb);
        entity.setProperty(ExerciseEntry.FIELD_NAME_AVG_SPEED, entry.mAvgSpeed);
        entity.setProperty(ExerciseEntry.FIELD_NAME_HEARTRATE, entry.mHeartRate);
        entity.setProperty(ExerciseEntry.FIELD_NAME_INPUT_TYPE, entry.mInputType);
        entity.setProperty(ExerciseEntry.FIELD_NAME_COMMENT, entry.mComment);

        mDatastore.put(entity);

        return true;
    }

    //get exercise entry by the id
    public static ExerciseEntry getEntryByName(String name, Transaction txn) {
        Entity result;
        try {
            result = mDatastore.get(KeyFactory.createKey(getKey(),
                    ExerciseEntry.ENTRY_ENTITY_NAME, name));
        } catch (Exception ex) {
            result = null;
        }

        return getEntryFromEntity(result);
    }

    //get exercise entry
    private static ExerciseEntry getEntryFromEntity(Entity entity) {
        if (entity == null) {
            return null;
        }

        //create new exercise entry Entity
        return new ExerciseEntry(
                (String) entity.getProperty(ExerciseEntry.FIELD_ID_NAME),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_ACTIVITY),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_DURATION),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_DISTANCE),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_DATE_TIME),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_AVG_SPEED),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_CALORIES),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_CLIMB),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_HEARTRATE),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_INPUT_TYPE),
                (String) entity.getProperty(ExerciseEntry.FIELD_NAME_COMMENT));

    }

    //deletes an exercise entry from datastore
    public static boolean delete(String name) {

        // query
        Query.Filter filter = new Query.FilterPredicate(ExerciseEntry.FIELD_ID_NAME,
                Query.FilterOperator.EQUAL, name);

        Query query = new Query(ExerciseEntry.ENTRY_ENTITY_NAME);
        query.setFilter(filter);

        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = mDatastore.prepare(query);

        Entity result = pq.asSingleEntity();
        boolean ret = false;
        if (result != null) {
            // delete
            mDatastore.delete(result.getKey());
            ret = true;
        }

        return ret;
    }

    //return an array list of exercise entries from data in datastore
    public static ArrayList<ExerciseEntry> query(String name) {
        ArrayList<ExerciseEntry> resultList = new ArrayList<ExerciseEntry>();
        if (name != null && !name.equals("")) {
            ExerciseEntry exerciseEntry = getEntryByName(name, null);
            if (exerciseEntry != null) {
                resultList.add(exerciseEntry);
            }
        } else {
            Query query = new Query(ExerciseEntry.ENTRY_ENTITY_NAME);
            // get every record from datastore, no filter
            query.setFilter(null);
            // set query's ancestor to get strong consistency
            query.setAncestor(getKey());

            PreparedQuery pq = mDatastore.prepare(query);

            for (Entity entity : pq.asIterable()) {
                ExerciseEntry entry = getEntryFromEntity(entity);
                if (entry != null) {
                    resultList.add(entry);
                }
            }
        }
        return resultList;
    }
}
