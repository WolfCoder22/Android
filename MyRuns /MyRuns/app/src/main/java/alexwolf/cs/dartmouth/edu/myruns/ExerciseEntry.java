package alexwolf.cs.dartmouth.edu.myruns;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by alexwolf on 1/26/16.
 */
public class ExerciseEntry {
    private Long id;

    private int mActivityType;     // Type of activity
    private int mDuration;
    private double mDistance;      // Distance traveled. Either in meters or feet.
    private double mAvgPace;
    private Calendar mDateTime;    // Time of Entry
    private double mAvgSpeed;
    private int mCalorie;
    private double mClimb;         // Climb ( meters or feet)
    private int mHeartRate;
    private int mInputType;        // Manual, GPS or automatic
    private String mComment;
    private Double mCurrSpeed;
    private ArrayList<LatLng> mLocationList; // Location list

    public ExerciseEntry(){
        mDateTime= Calendar.getInstance();
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public int getmInputType() {
        return mInputType;
    }

    public void setmInputType(int mInputType) {
        this.mInputType = mInputType;
    }

    public int getmActivityType() {
        return mActivityType;
    }

    public void setmActivityType(int mActivityType) {
        this.mActivityType = mActivityType;
    }

    public Calendar getmDateTime() {
        return mDateTime;
    }

    //convert long (timeMills) into Calender
    public void setmDateTime(Long timeMills) {
        mDateTime.setTimeInMillis(timeMills);
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public double getmDistance() {
        return mDistance;
    }

    public void setmDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public double getmAvgPace() {
        return mAvgPace;
    }

    public void setmAvgPace(double mAvgPace) {
        this.mAvgPace = mAvgPace;
    }

    public double getmAvgSpeed() {
        return mAvgSpeed;
    }

    public void setmAvgSpeed(double mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public int getmCalorie() {
        return mCalorie;
    }

    public void setmCalorie(int mCalorie) {
        this.mCalorie = mCalorie;
    }

    public double getmClimb() {
        return mClimb;
    }

    public void setmClimb(double mClimb) {
        this.mClimb = mClimb;
    }

    public int getmHeartRate() {
        return mHeartRate;
    }

    public void setmHeartRate(int mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<LatLng> getmLocationList() {
        return mLocationList;
    }

    public void setmLocationList(ArrayList<LatLng> mLocationList) {
        this.mLocationList = mLocationList;
    }

    public Double getmCurrSpeed() {
        return mCurrSpeed;
    }

    public void setmCurrSpeed(Double mCurrSpeed) {
        this.mCurrSpeed = mCurrSpeed;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();

        //get strings for date
        String date = new SimpleDateFormat("MMM d, yyyy", Locale.US).format(mDateTime.getTime());
        String entryTime = new SimpleDateFormat("HH:mm:ss", Locale.US).format(mDateTime.getTime());

        //get activity and input type
        String input=EntryDisplay.getInputTypeString(getmInputType());
        String activity=EntryDisplay.getActivityTypeString(getmActivityType());

        //get the comment handling if null
        String comment;
        if(getmComment()==null){
            comment= "None";
        }
        else{
            comment=getmComment();
        }

        try {
            obj.put("id", Long.toString(getId()));
            obj.put("inputType", input);
            obj.put("activity", activity);
            obj.put("dateTime", entryTime+" "+date);
            obj.put("dur", Integer.toString(getmDuration()));
            obj.put("distance", Double.toString(getmDistance()));
            obj.put("avgSpeed", Double.toString(getmAvgSpeed()));
            obj.put("calorie", Integer.toString(getmCalorie()));
            obj.put("climb", Double.toString(getmClimb()));
            obj.put("heartrate", Integer.toString(getmHeartRate()));
            obj.put("comment",comment);
        } catch (JSONException e) {
            return null;
        }

        return obj;
    }
}