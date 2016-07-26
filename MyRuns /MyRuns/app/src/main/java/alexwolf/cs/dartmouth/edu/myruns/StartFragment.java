package alexwolf.cs.dartmouth.edu.myruns;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by alexwolf on 1/15/16.
 */
public class StartFragment extends Fragment implements FragmentInterface{
    public Button mStartButton;
    public Button mSyncButton;
    private String mInputType;
    private String mActivtyType;
    private Spinner mInputTypeSpinner;
    private Spinner mActivityTypeSpinner;
    protected View inflatedView;
    private EntrySQLiteHelper SQlite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.start_layout, container, false);

        //wire buttons
        mStartButton= (Button) inflatedView.findViewById(R.id.startButton);
        mSyncButton= (Button) inflatedView.findViewById(R.id.syncButton);

        //Locate the Input and Activity Spinner
        mInputTypeSpinner=(Spinner) inflatedView.findViewById(R.id.inputType);
        mActivityTypeSpinner=(Spinner) inflatedView.findViewById(R.id.activityType);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get Input and Activity Type from Spinner
                mInputType= mInputTypeSpinner.getSelectedItem().toString();
                mActivtyType=mActivityTypeSpinner.getSelectedItem().toString();
                int activityInt= getActivityTypeInt(mActivtyType);


                //for each input type
                switch(mInputType){

                    //start manual entry activity
                    case "Manual Entry":{
                        //Create Manual Input Activity intent
                        Intent intent = new Intent(getActivity(), ManualInputActivity.class);

                        //pass manual input type(int) to the intent
                        intent.putExtra("Input_Type", 0);
                        intent.putExtra("Activity_Type", activityInt);

                        startActivity(intent);
                        break;
                    }

                    //start map display activity
                    case "GPS":{

                        //create intent to start map activity
                        Intent intent = new Intent(getActivity(), MapDisplayActivity.class);

                        //bind data to the intent
                        intent.putExtra("input_Type", 1);
                        intent.putExtra("activity_Type", activityInt);

                        startActivity(intent);
                        break;
                    }

                    //start map display activity
                    case "Automatic":{

                        //create new intent
                        Intent intent = new Intent(getActivity(), MapDisplayActivity.class);

                        //bind data to the intent
                        intent.putExtra("input_Type", 2);

                        startActivity(intent);
                        break;
                    }
                }


            }
        });

        mSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //async task for loading entries
                new AsyncTask<Void, Void, String>(){

                    @Override
                    protected String doInBackground(Void... params) {

                        //get the database helper
                        SQlite= new EntrySQLiteHelper(getActivity().getApplicationContext());

                        //get entry array from Database
                        ExerciseEntry[] entries= SQlite.fetchEntries();
                        Log.d("jfjf||   ", Integer.toString(entries.length));

                        JSONArray jsons= new JSONArray();

                        //upload all entries to database
                        for(ExerciseEntry entry: entries){

                            //get the JSON for each entry and add to array
                            JSONObject json= entry.toJSONObject();
                            jsons.put(json);
                        }


                        //create Hasmap for posting to server
                        HashMap<String, String> JSONMAP= new HashMap<String, String>();

                        //add string of jsonArray of Entries
                        JSONMAP.put("entries", jsons.toString());

                        //post data to server
                        try {
                            ServerUtilities.post("https://propane-analogy-121223.appspot.com"+"/add.do", JSONMAP);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //close the database
                        SQlite.close();
                        return null;
                    }

//                    @Override
//                    protected void onPostExecute(String s) {
//                        String resultString;
//                        if(s.equals("")) {
//                            resultString =  " entry uploaded.";
//                        } else {
//                            resultString = s;
//                        }
//
//                        Toast.makeText(getActivity().getApplicationContext(), resultString,
//                                Toast.LENGTH_SHORT).show();
//
//                    }
                }.execute();

            }
        });

        return inflatedView;
    }

    //method for finding an activity types int
    public static int getActivityTypeInt(String activity) {
        switch (activity) {
            case "Running":
                return 0;
            case "Walking":
                return 1;
            case "Standing":
                return 2;
            case "Cycling":
                return 3;
            case "Hiking":
                return 4;
            case "Downhill Skiing":
                return 5;
            case "Cross-Country Skiing":
                return 6;
            case "Snowboarding":
                return 7;
            case "Skating":
                return 8;
            case "Swimming":
                return 9;
            case "Mountain Biking":
                return 10;
            case "Wheelchair":
                return 11;
            case "Eliptical":
                return 12;
            case "Other":
                return 13;
        }
        return 13;
    }

    @Override
    public void fragmentBecameVisible() {
        //do nothing
    }
}


