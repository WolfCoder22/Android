package alexwolf.cs.dartmouth.edu.myruns;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by alexwolf on 1/31/16.
 */
public class ExerciseArrayAdaptor extends TwoLineArrayAdapter<ExerciseEntry> {
    String mUnitType;


    public ExerciseArrayAdaptor(Context context, ExerciseEntry[] exerciseEntrys, String mUnit) {
        super(context, exerciseEntrys);
        mUnitType=mUnit;

    }

    @Override   //get string for Line1 Text
    public String lineOneText(ExerciseEntry exerciseEntry) {

        //get data for the string
        int inputType= exerciseEntry.getmInputType();
        int activityType= exerciseEntry.getmActivityType();
        Calendar dateTime= exerciseEntry.getmDateTime();

        return getInputTypeString(inputType)+": "+getActivityTypeString(activityType)+", "+getDateString(dateTime);
    }

    @Override //get and return string for Line2 Text
    public String lineTwoText(ExerciseEntry exerciseEntry) {

        //get time in seconds
        String duration= Integer.toString(exerciseEntry.getmDuration());
        return getDistance(exerciseEntry)+", "+duration+" Seconds";
    }

    //method to get String based off input number
    public String getInputTypeString(int i){
        switch(i){
            case 0:
                return "Manual Entry";
            case 1:
                return "GPS";
            case 2:
                return "Automatic";

        }
        return null;
    }

    //method for getting an activity string from an int
    public String getActivityTypeString(int i){
        switch(i){
            case 0:
                return "Running";
            case 1:
                return "Walking";
            case 2:
                return "Standing";
            case 3:
                return "Cycling";
            case 4:
                return "Hiking";
            case 5:
                return "Downhill Skiing";
            case 6:
                return "Cross-Country Skiing";
            case 7:
                return "Snowboarding";
            case 8:
                return "Skating";
            case 9:
                return "Swimming";
            case 10:
                return "Mountain Biking";
            case 11:
                return "Wheelchair";
            case 12:
                return "Eliptical";
            case 13:
                return "Other";
        }
        return null;
    }

    //get datetime in string from the calendar
    public String getDateString(Calendar c){

        //get all time data
        String mYear = Integer.toString(c.get(Calendar.YEAR));
        int mMonth = c.get(Calendar.MONTH);
        String mDay = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String mMinute = Integer.toString(c.get(Calendar.MINUTE));
        String mHour = Integer.toString(c.get(Calendar.HOUR));
        String mSecond= Integer.toString(c.get(Calendar.SECOND));

        //create string for Month in words and get it based off the int
        String mMonthWord= null;
        switch(mMonth){
            case 0:
                mMonthWord= "Jan";
                break;
            case 1:
                mMonthWord= "Feb";
                break;
            case 2:
                mMonthWord= "March";
                break;
            case 3:
                mMonthWord= "April";
                break;
            case 4:
                mMonthWord= "May";
                break;
            case 5:
                mMonthWord= "June";
                break;
            case 6:
                mMonthWord= "July";
                break;
            case 7:
                mMonthWord= "August";
                break;
            case 8:
                mMonthWord= "Sept";
                break;
            case 9:
                mMonthWord= "Oct";
                break;
            case 10:
                mMonthWord= "Nov";
                break;
            case 11:
                mMonthWord= "Dec";
        }

        //return the DateTime String for Display
        return mHour+":"+mMinute+":"+mSecond+" "+mMonthWord+ " "+mDay+" "+mYear;

    }

    //method to get string of Unit Distance
    public String getDistance(ExerciseEntry exerciseEntry){

        //get exerciseEntry distance
        double miles= exerciseEntry.getmDistance();

        //if its miles return Distance in Miles
        if(mUnitType.length()==5){
            return Integer.toString((int) miles )+" Miles";
        }
        //if unitType is Kilometers convert distance to kilometers and get String
        else{
            Double kilometers= miles*1.60934;
            return Integer.toString(kilometers.intValue())+" Kilometers";
        }
    }

}
