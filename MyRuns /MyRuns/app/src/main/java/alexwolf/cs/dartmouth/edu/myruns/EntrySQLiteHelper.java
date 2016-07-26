package alexwolf.cs.dartmouth.edu.myruns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;


/**
 * Created by alexwolf on 1/28/16.
 */
public class EntrySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Exercise_Entries.db";
    private static final int DATABASE_VERSION = 1;

    // Keys for sql database
    public static final String ENTRIES = "ENTRIES";

    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_INPUT_TYPE = "input_type";
    public static final String KEY_ACTIVITY_TYPE = "activity";
    public static final String KEY_DATE_TIME = "date_time";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_CALORIES = "calories";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_HEART_RATE = "heart_rate";
    public static final String KEY_CLIMB = "climb";
    public static final String KEY_AVG_PACE = "avg_pace";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_AVG_SPEED = "avg_speed";
    public static final String KEY_PRIVACY= "privacy";
    public static final String KEY_GPS_DATA= "gps";

    public SQLiteDatabase db;




    //Constructor giving DatabaseName and Version
    public EntrySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db=  getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Database creation statement
         final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
                + ENTRIES+
                " ( "
                + KEY_ROW_ID
                +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +KEY_INPUT_TYPE
                +" INTEGER NOT NULL, "
                +KEY_ACTIVITY_TYPE
                +" INTEGER NOT NULL, "
                +KEY_DATE_TIME
                +" DATETIME NOT NULL, "
                +KEY_DURATION
                +" INTEGER NOT NULL, "
                +KEY_DISTANCE
                +" DOUBLE, "
                +KEY_CALORIES
                +" INTEGER NOT NULL, "
                +KEY_HEART_RATE
                +" INTEGER NOT NULL, "
                +KEY_CLIMB
                +" DOUBLE, "
                +KEY_AVG_PACE
                +" DOUBLE, "
                +KEY_COMMENT
                +" TEXT, "
                +KEY_AVG_SPEED
                +" DOUBLE, "
                +KEY_PRIVACY
                +" INTEGER, "
                +KEY_GPS_DATA
                +" BLOB "
                +");";

        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(EntrySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ENTRIES);
        onCreate(db);

    }

    // Insert a item given each column value
    public long insertEntry(ExerciseEntry entry) {

        //create content values
        ContentValues values= getValues(entry);

        //insert new entry and create an entryID
        long entryId = db.insert(ENTRIES, null, values);


        //return entry id number
        return entryId;
    }


    // Remove an entry by giving its index
    public void removeEntry(Long rowId) {
        Log.d("databse||", Long.toString(rowId));
        //delete specific row in the database
        db.delete(ENTRIES, KEY_ROW_ID + " = " + rowId, null);

    }

    // Query a specific entry by its index.
    public ExerciseEntry fetchEntryByIndex(long rowId) {


        //query the database for that specific row
        Cursor cursor = db.query(ENTRIES,   //the MySQLTable
                null,                                            //query all columns
                KEY_ROW_ID + " = ?",                                 //query a specific row
                new String[]{String.valueOf(rowId)},             //with this id
                null,
                null,
                null,
                "1");                                           //limit 1 row

        cursor.moveToFirst();

        //Get Exercise Entry Object with the query data
        ExerciseEntry entry= setEntryData(cursor);

        //close database and cursor
        cursor.close();

        return entry;
    }


    // Query the entire table, return all rows
    public ExerciseEntry[] fetchEntries() {

        //query the database of all data
        Cursor cursor= db.query(ENTRIES, null, null, null, null, null, null);

        //create array for entries
        ExerciseEntry[] entries= new ExerciseEntry[(int) DatabaseUtils.queryNumEntries(db, ENTRIES)];


        //moveCursorToFirst
        cursor.moveToFirst();

        //intilize collumn counter
        int collum=0;

        //loop through all row
        while(!cursor.isAfterLast()){

            //create new entry and bind row data to it
            ExerciseEntry entry= setEntryData(cursor);

            //move to next row
            cursor.moveToNext();

            //add entry to arrayList
            entries[collum]=entry;

            //update count
            collum+=1;

        }

        //close the cursor and move to forst
        cursor.moveToFirst();
        cursor.close();

        return entries;
    }



    //method to get content values
    public ContentValues getValues(ExerciseEntry entry){

        //initiate values
        ContentValues values = new ContentValues();

        //retrieve value data from entry
        int mActivityType= entry.getmActivityType();
        int mInputType = entry.getmInputType();
        Long mDateTime= entry.getmDateTime().getTimeInMillis();
        int mDuration= entry.getmDuration();
        float mDistance= (float) entry.getmDistance();
        int mCalories= entry.getmCalorie();
        int mHeartRate= entry.getmHeartRate();
        double mClimb= entry.getmClimb();
        String mComment= entry.getmComment();
        Double mAvgSpeed= entry.getmAvgSpeed();
        Double mAvgPace= entry.getmAvgPace();

        //get location byte array if gps input
        if (mInputType!=0){
            byte[] mLocations= getLocationByteArray(entry.getmLocationList());
            values.put(KEY_GPS_DATA, mLocations);
        }

        //add data to values
        values.put(KEY_ACTIVITY_TYPE, mActivityType);
        values.put(KEY_INPUT_TYPE, mInputType);
        values.put(KEY_DURATION, mDuration);
        values.put(KEY_DISTANCE, mDistance);
        values.put(KEY_CALORIES, mCalories);
        values.put(KEY_HEART_RATE, mHeartRate);
        values.put(KEY_AVG_PACE, mAvgPace);
        values.put(KEY_CLIMB, mClimb);
        values.put(KEY_COMMENT, mComment);
        values.put(KEY_AVG_SPEED, mAvgSpeed);
        values.put(KEY_DATE_TIME, mDateTime);

        return values;
    }

    //set entry Data from database
    public ExerciseEntry setEntryData(Cursor cursor){

        //create new exercise entry
        ExerciseEntry entry= new ExerciseEntry();

        //Set up entry data
        entry.setId(cursor.getLong(0));
        entry.setmInputType(cursor.getInt(1));
        entry.setmActivityType(cursor.getInt(2));
        entry.setmDateTime(cursor.getLong(3));
        entry.setmDuration(cursor.getInt(4));
        entry.setmDistance(cursor.getDouble(5));
        entry.setmCalorie(cursor.getInt(6));
        entry.setmHeartRate(cursor.getInt(7));
        entry.setmClimb(cursor.getDouble(8));
        entry.setmAvgPace(cursor.getDouble(9));
        entry.setmComment(cursor.getString(10));
        entry.setmAvgSpeed(cursor.getDouble(11));

        //if it's a map location set entry type
        if(cursor.getInt(1)!=0){
            entry.setmLocationList(setLocationListFromByteArray(cursor.getBlob(13)));
        }


        //return the entry
        return entry;


    }

    // Convert Location ArrayList to byte array, to store in SQLite database
    public byte[] getLocationByteArray(ArrayList<LatLng> mLocationLatLngList) {
        int[] intArray = new int[mLocationLatLngList.size() * 2];

        for (int i = 0; i < mLocationLatLngList.size(); i++) {
            intArray[i * 2] = (int) (mLocationLatLngList.get(i).latitude * 1E6);
            intArray[(i * 2) + 1] = (int) (mLocationLatLngList.get(i).longitude * 1E6);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length
                * Integer.SIZE);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(intArray);

        return byteBuffer.array();
    }

    // Convert byte array to Location ArrayList
    public ArrayList<LatLng> setLocationListFromByteArray(byte[] bytePointArray) {

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytePointArray);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();

        int[] intArray = new int[bytePointArray.length / Integer.SIZE];
        intBuffer.get(intArray);

        int locationNum = intArray.length / 2;

        //init array for latLngs
        ArrayList<LatLng> mlist= new ArrayList<LatLng>();

        for (int i = 0; i < locationNum; i++) {
            LatLng latLng = new LatLng((double) intArray[i * 2] / 1E6F,
                    (double) intArray[i * 2 + 1] / 1E6F);
            mlist.add(latLng);
        }

        return mlist;
    }
}
