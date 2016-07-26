package edu.dartmouth.cs.alexwolf.myruns.backend.data;

/**
 * Created by alexwolf on 2/20/16.
 */
public class ExerciseEntry {
    public static final String EXERCISE_ENTRY_PARENT_ENTITY_NAME = "ExerciseEntryParent";
    public static final String EXERCISE_ENTRY_PARENT_KEY_NAME = "ExerciseEntryParent";

    public static final String ENTRY_ENTITY_NAME = "ExerciseEntry";

    //fields used to access data from entity
    public static final String FIELD_ID_NAME = "id";
    public static final String FIELD_NAME_ACTIVITY = "activity";
    public static final String FIELD_NAME_DURATION = "dur";
    public static final String FIELD_NAME_DISTANCE = "distance";
    public static final String FIELD_NAME_DATE_TIME= "dateTime";
    public static final String FIELD_NAME_CALORIES = "calorie";
    public static final String FIELD_NAME_CLIMB = "climb";
    public static final String FIELD_NAME_HEARTRATE = "heartrate";
    public static final String FIELD_NAME_INPUT_TYPE = "inputType";
    public static final String FIELD_NAME_COMMENT = "comment";
    public static final String FIELD_NAME_AVG_SPEED = "avgSpeed";

    public String mId;
    public String mActivityType;     // Type of activity
    public String mDuration;
    public String mDistance;      // Distance traveled. Either in meters or feet.;
    public String mDateTime;    // Time of Entry
    public String mAvgSpeed;
    public String mCalorie;
    public String mClimb;         // Climb ( meters or feet)
    public String mHeartRate;
    public String mInputType;        // Manual, GPS or automatic
    public String mComment;

    //constructor
    public ExerciseEntry(String id, String activityType, String duration, String distance,String dateTime, String avgSpeed,
                         String calories, String climb, String heartrate, String inputType, String comment){

        //set Entry instance variables
        mId=id;
        mActivityType=activityType;
        mDuration= duration;
        mDistance= distance;
        mDateTime= dateTime;
        mAvgSpeed= avgSpeed;
        mCalorie= calories;
        mClimb= climb;
        mHeartRate= heartrate;
        mInputType= inputType;
        mComment= comment;


    }

}
