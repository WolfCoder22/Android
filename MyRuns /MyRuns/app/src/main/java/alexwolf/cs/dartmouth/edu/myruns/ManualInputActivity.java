package alexwolf.cs.dartmouth.edu.myruns;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


/**
 * Created by alexwolf on 1/16/16.
 */
public class ManualInputActivity extends Activity {

    //string array of data entry types
    static final String[] ENTRIES = new String[] { "Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment" };
    public EntrySQLiteHelper SQLite;
    private ExerciseEntry exerciseEntry;
    private String mDateTime;
    private String mDayTime;
    private boolean notTimeDatPicker=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //gte the database helper
        SQLite= new EntrySQLiteHelper(getApplicationContext());

        //create new Exercise Entry
        exerciseEntry= new ExerciseEntry();

        //get activity type and input type and bind it to the Exercise Entry
        exerciseEntry.setmInputType(getIntent().getIntExtra("Input_Type", 0));
        exerciseEntry.setmActivityType(getIntent().getIntExtra("Activity_Type", 13));

        //Get Current time for mDateTime and mDayTime
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mMinute = c.get(Calendar.MINUTE);
        int mHour = c.get(Calendar.HOUR);
        int mSecond= c.get(Calendar.SECOND);

        //set DateTime and dayTime to current time
        mDateTime= Integer.toString(mYear)+"-"+Integer.toString(mMonth)+"-"+Integer.toString(mDay);
        mDayTime= Integer.toString(mHour)+":"+Integer.toString(mMinute)+":"+Integer.toString(mSecond);

        //set up the view
        setContentView(R.layout.listview_layout);

        // Define a new adapter for listview
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(ManualInputActivity.this,
                R.layout.textview_for_entries, ENTRIES);

        // Retrive Listview and Assign the adapter to ListView
        ListView listView = (ListView) findViewById(R.id.datalistview);
        listView.setAdapter(mAdapter);


        // Define the listener interface
        OnItemClickListener mListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String entry= ((TextView) view).getText().toString();

                //start dialog fragment
                startEntryDialog(entry);
            }};

        // Wire ListView to the listener
        listView.setOnItemClickListener(mListener);
    }

    //start dialog based off click of entry type
    private void startEntryDialog(final String entry) {

        //create an alert dialog
        final AlertDialog.Builder myAlertDialog =new AlertDialog.Builder(ManualInputActivity.this);

        //Get Current time from android calender for date and time picker
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mMinute = c.get(Calendar.MINUTE);
        int mHour = c.get(Calendar.HOUR);

        //create edittext for the dialog
        final EditText input = new EditText(this);

        //instantiate myDialog for each entry type
        switch(entry){
            case "Date":{
                //set not timepicker to false
                notTimeDatPicker=false;

                //create a date picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            //called when user selects hits ok
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //update the date time
                                mDateTime= Integer.toString(year)+"-"+Integer.toString(monthOfYear)+"-"+Integer.toString(dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);

                //show the dialog
                dpd.show();
                break;
            }

            case "Time":{

                //set not timepicker to false
                notTimeDatPicker=false;

                //create time picker dialog
                TimePickerDialog tpd = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            //called when user hits ok
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                //update the day Time
                                mDayTime= Integer.toString(hourOfDay)+":"+Integer.toString(minute)+":"+Integer.toString(00);
                            }
                        }, mHour, mMinute, false);

                //show the dialog
                tpd.show();
                break;
            }

            case "Duration":{
                //set not timepicker to true
                notTimeDatPicker=true;

                //Set Dialog Title
                myAlertDialog.setTitle(R.string.duration);

                // Set up textInput
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setId(R.id.Duration);

                //set the view and show the Dialog
                myAlertDialog.setView(input);
                break;
            }
            case "Distance":{
                //set not timepicker to true
                notTimeDatPicker=true;

                //Set Dialog Title
                myAlertDialog.setTitle(R.string.distance);

                // Set up number Input
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setId(R.id.Distance);

                //set the view and show the Dialog
                myAlertDialog.setView(input);
                break;
            }
            case "Calories":{
                //set not timepicker to true
                notTimeDatPicker=true;

                //Set Dialog Title
                myAlertDialog.setTitle(R.string.calories);

                // Set up number input
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setId(R.id.Calories);

                //set the view and show the Dialog
                myAlertDialog.setView(input);
                break;
            }
            case "Heart Rate":{

                //set not timepicker to true
                notTimeDatPicker=true;

                //Set Dialog Title
                myAlertDialog.setTitle(R.string.heart_rate);

                // Set up the number input
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setId(R.id.HeartRate);

                //set the view and show the Dialog
                myAlertDialog.setView(input);
                break;
            }

            case "Comment":{
                //set not timepicker to true
                notTimeDatPicker=true;

                //Set Dialog Title
                myAlertDialog.setTitle(R.string.comment);

                // Set up text input with hint
                input.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                input.setHint(R.string.class_comment);
                input.setId(R.id.comment);

                //set the view and show the Dialog
                myAlertDialog.setView(input);
                break;
            }

        }

        //set up onclick listener for ok button to bind user input to exercise entry for every dialog except time and date picker
        if(notTimeDatPicker) {
            myAlertDialog.setPositiveButton(
                    R.string.okButtonString,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {

                            //get data from the Different alertDialogs and set it to the exercise entry
                            if (Objects.equals(entry, "Duration")) {
                                int duration = Integer.parseInt(input.getText().toString());
                                exerciseEntry.setmDuration(duration);

                            } else if (Objects.equals(entry, "Distance")) {
                                int distance = Integer.parseInt(input.getText().toString());
                                exerciseEntry.setmDistance(distance);

                            } else if (Objects.equals(entry, "Calories")) {
                                int calories = Integer.parseInt(input.getText().toString());
                                exerciseEntry.setmCalorie(calories);

                            } else if (Objects.equals(entry, "Heart Rate")) {
                                int heartRate = Integer.parseInt(input.getText().toString());
                                exerciseEntry.setmHeartRate(heartRate);

                            } else if (Objects.equals(entry, "Comment")) {
                                String comment = input.getText().toString();
                                exerciseEntry.setmComment(comment);
                            }

                        }
                    }
            );

            //set up onclick listener for cancel button
            myAlertDialog.setNegativeButton(
                    R.string.cancelButtonString,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {

                        }
                    }
            );

            //display the dialog
            myAlertDialog.show();
        }
    }

    public void saveData(View view) throws ParseException {

        //get the timeMills from the date chosen
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = format.parse(mDateTime+" "+mDayTime);
        long timeMills = date.getTime();

        //set the time the exercise entry
        exerciseEntry.setmDateTime(timeMills);


        //execute database insertion on a asynctask
        InsertEntryTask task=new InsertEntryTask();
        task.doInBackground();


        Toast.makeText(ManualInputActivity.this, "Data Entry #"+Long.toString(exerciseEntry.getId())+" Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelData(View view){
        Toast.makeText(ManualInputActivity.this, "Entry Discarded", Toast.LENGTH_SHORT).show();
        finish();
    }

    public class InsertEntryTask extends AsyncTask<Void, String, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            long id=SQLite.insertEntry(exerciseEntry);
            exerciseEntry.setId(id);
            return null;
        }
    }
}

