package alexwolf.cs.dartmouth.edu.myruns;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by alexwolf on 2/1/16.
 * Class for display a specific exercise entry
 */
public class EntryDisplay extends Activity{
    public ExerciseEntry entry;
    public EntrySQLiteHelper Sqlite;
    private String unitType;
    private Long entryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_display_layout);

        //retrieve all editTexts in the View for setting Text
        EditText inputTypeText= (EditText) findViewById(R.id.inputTypeEdit);
        EditText activityTypeText= (EditText) findViewById(R.id.activityTypeEdit);
        EditText dateText= (EditText) findViewById(R.id.dateEdit);
        EditText durationText= (EditText) findViewById(R.id.durationEdit);
        EditText distanceText= (EditText) findViewById(R.id.distanceEdit);
        EditText caloriesText= (EditText) findViewById(R.id.caloriesEdit);
        EditText heartRateText= (EditText) findViewById(R.id.heartRateEdit);

        //get the entry id from the intent and unit Type
        entryID=getIntent().getLongExtra("entryID", 2);
        unitType= getIntent().getStringExtra("unitType");


        //get the SQLhelper
        Sqlite= new EntrySQLiteHelper(this);

        //get the entry with the associatted ID
        entry= Sqlite.fetchEntryByIndex(entryID);

        //get data to set to editTexts
        String inputType= getInputTypeString(entry.getmInputType());
        String activityType= getActivityTypeString(entry.getmActivityType());
        String date= getDateString(entry.getmDateTime());
        String duration= Integer.toString(entry.getmDuration());
        String distance= getDistance(entry);
        String calories= Integer.toString(entry.getmCalorie());
        String heartRate= Integer.toString(entry.getmHeartRate());

        //set entry data to the editTexts
        inputTypeText.setText(inputType);
        activityTypeText.setText(activityType);
        dateText.setText(date);
        durationText.setText(duration);
        distanceText.setText(distance);
        caloriesText.setText(calories);
        heartRateText.setText(heartRate);

    }


    //method to get String based off input number
    public static String getInputTypeString(int i){
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
    public static String getActivityTypeString(int i){
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
        if(unitType.length()==5){
            return Integer.toString((int) miles )+" Miles";
        }
        //if unitType is Kilometers convert distance to kilometers and get String
        else{
            Double kilometers= miles*1.60934;
            return Integer.toString(kilometers.intValue())+" Kilometers";
        }
    }

    @Override //inflates the menu with the Delete button to the view
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteButton:
                //delete entry on Async Task
                DeleteEntryTask task= new DeleteEntryTask();
                task.doInBackground();

                //exit the activity
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //async task to delete an extry
    public class DeleteEntryTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Sqlite.removeEntry(entryID);
            return null;
        }
    }
}
