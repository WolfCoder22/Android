package alexwolf.cs.dartmouth.edu.myruns;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by alexwolf on 1/17/16.
 */
public class MapDisplayActivity extends FragmentActivity{
    private TextView mTextView;
    GoogleMap mMap;
    String mUnit;
    int inputType;
    int activityType;
    private boolean mIsBound;
    ExerciseEntry mEntry;
    private boolean mIsHistoryEntry;
    private boolean mIsFromNotification;
    TrackingService mService;
    String TAG = "MapDisplayActivity";
    EntrySQLiteHelper SQ;
    public Marker startMarker;
    public Marker endMarker;
    public Polyline mLine;
    public boolean isRotated= false;
    private long mEntryID;
    public ServiceConnection mConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(getClass().getName(), "onServiceConnected");

            // get the service reference
            TrackingService.TrackingBinder binder = (TrackingService.TrackingBinder) service;
            mService = binder.getService();

            //update the entry in this class
            mEntry=mService.getExerciseEntry();

            //update the textview
            updateTextView();

            //update map if rotates
            if(isRotated && mEntry.getmLocationList().size()!=0){
                drawTraceOnMap();
                updateTextView();
                isRotated=false;
            }

            //draw trace on map if from connection
            if(mIsFromNotification && mEntry.getmLocationList().size()!=0){
                drawTraceOnMap();
                updateTextView();
                mIsFromNotification=false;
            }
            mIsBound= false;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(getClass().getName(), "onServiceDisconnected");
        }
    };

    //Set the view for the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(FEATURE)

        //get the database
        SQ = new EntrySQLiteHelper(this.getApplicationContext());

        //get latest unit Type(miles or kilometers
        updateUnitType();

        //Set up the view
        setContentView(R.layout.map_display_layout);

        //set up the map
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //check is displaying a history entry or creating a new one
        mIsHistoryEntry = getIntent().getBooleanExtra("isHistoryEntry", false);
        mIsFromNotification= getIntent().getBooleanExtra("fromNotification", false);


        //if displaying a history entry
        if (mIsHistoryEntry) {

            //disable the save and cancel buttons
            Button saveButton= (Button) findViewById(R.id.saveButtonMap);
            Button cancelButton= (Button) findViewById(R.id.cancelButtonMap);
            saveButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);

            //get the entryID
            mEntryID = getIntent().getLongExtra("entryId", -1);

            //get the exercise entry in AsyncLoader
            AsyncTaskLoader<ExerciseEntry> loader= new EntryLoader(this);
            mEntry=loader.loadInBackground();

            //animate camera towards path and draw trace
            try{
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mEntry.getmLocationList().get(0), 18));
                drawTraceOnMap();
            }catch(Exception e){
                Log.d(getLocalClassName(),"no locations saved");
            }


            //update the text view
            updateTextView();

        }

        //is an active entry recording
        else{

            mIsBound= false;
            //don't start the service again if orientation changes
            if(savedInstanceState!=null) {
                isRotated=true;
            }

            //create a new GPS/Automatic Entry if not from history or phone not rotated or from notification
            else if(!isRotated && !mIsFromNotification) {

                //Register the reciever with the broadcast manager with specific action intent
                LocalBroadcastManager.getInstance(this).registerReceiver(mTrackReceiver,
                        new IntentFilter("get_data"));

                //get the activity and input type
                inputType = getIntent().getIntExtra("input_Type", -1);

                activityType = getIntent().getIntExtra("activity_Type", -1);

                //create new intent and bind data
                Intent i =new Intent(MapDisplayActivity.this, TrackingService.class);
                i.putExtra("inputType", inputType);
                i.putExtra("activityType", activityType);

                //start the service
                Log.d(TAG, "startService()");
                startService(i);

            }

        }
    }

    //update the textview
    private void updateTextView() {

        //get the text view
        mTextView= (TextView) findViewById(R.id.textMapDisplay);

        //get strin array of activities
        String[] activityTypes= getApplicationContext().getResources().getStringArray(R.array.activity_type_data_entries);

        //init string for text view
        String text;

        //string for current speed if displayng history or current entry
        String mCurrSpeed;

        if(mIsHistoryEntry){
            mCurrSpeed="n/a";
        }
        //if in miles
        else if(mUnit.length()==5){
            mCurrSpeed=Double.toString(mEntry.getmCurrSpeed())+" m/h";
        }
        //in kilometers
        else{
           mCurrSpeed=Double.toString(1.60934*mEntry.getmCurrSpeed())+" km/h";
        }

        //if unit type set to miles
        if(mUnit.length()==5){
            text="Type: "+activityTypes[mEntry.getmActivityType()]+"\n"+
                    "Avg speed: "+Double.toString(Math.round(mEntry.getmAvgSpeed() * 100d) / 100d)+" m/h"+"\n"+
                    "Current Speed: "+ mCurrSpeed+"\n"+
                    "Climb: "+Double.toString(mEntry.getmClimb())+" Miles"+"\n"+
                    "Calories: "+Integer.toString(mEntry.getmCalorie())+"\n"+
                    "Distance: "+Double.toString(Math.round(mEntry.getmDistance() * 100) / 100)+" Miles";
        }

        //unit set to kilometers
        else{
            text="Type: "+activityTypes[mEntry.getmActivityType()]+"\n"+
                    "Avg speed: "+Double.toString(Math.round((mEntry.getmAvgSpeed() * 100d) / 100d)*1.60934)+" km/h"+"\n"+
                    "Current Speed: "+ mCurrSpeed+"\n"+
                    "Climb: "+Double.toString(1.60934*mEntry.getmClimb())+" Kilometers"+"\n"+
                    "Calories: "+Integer.toString(mEntry.getmCalorie())+"\n"+
                    "Distance: "+Double.toString(1.60934*(Math.round(mEntry.getmDistance() * 100) / 100))+" Kilometers";
        }

        //set the text to the view
        mTextView.setText(text);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the service hasn't been bound yet
        if(!mIsBound && !mIsHistoryEntry) {
            //create intent to access the service
            Intent i = new Intent(this, TrackingService.class);

            bindService(i, mConnection, Context.BIND_AUTO_CREATE);
            Log.d("", "service bound");
            mIsBound=true;
        }

    }


    //Draws trace on map and sets up markers
    private void drawTraceOnMap() {

        //remove the previuos polyLine and endMarker if exist
        if(mLine!=null){
            mLine.remove();
        }
        if(endMarker!=null){
            endMarker.remove();
        }

        //get size of location list of entry
        int size= mEntry.getmLocationList().size();
        Log.d("Locs Size", Integer.toString(size));
        ArrayList<LatLng> locs= mEntry.getmLocationList();

        //set the start marker
        startMarker= mMap.addMarker(new MarkerOptions().position(locs.get(0)).icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN)));

        //move camera towards first location(sometimes two get added
        if((size == 1) || size==2) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locs.get(0), 18));
        }

        //if there are multiple locations
        if(size>1){

            //set Red Marker on last/current location
            endMarker=mMap.addMarker(new MarkerOptions().position(locs.get(size-1)).icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED)));

            //if there is no line on the map
            Log.d("line", "drawline");

            //draw a polyLines of the location trace
            mLine = mMap.addPolyline(new PolylineOptions()
                        .addAll(locs)
                        .color(Color.BLACK)
                        .width(5)
                        .visible(true));




        }


    }

    //For now return back to main_cotent activity
    public void save(View view){

        //stop service
        stopService(new Intent(this, TrackingService.class));

        //unBind the service
        unbindService(mConnection);
        mIsBound=false;

        //unregister the reciever
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.unregisterReceiver(mTrackReceiver);

        //save entry to database
        long id=SQ.insertEntry(mEntry);

        Toast.makeText(MapDisplayActivity.this, "Data Entry #" + Long.toString(id) + " Saved", Toast.LENGTH_SHORT).show();

        //close the database
        SQ.close();

        //create intent to go back to mainactivity
        Intent intent = new Intent(MapDisplayActivity.this, MainActivity.class);
        startActivity(intent);

    }

    //For onCLick: Cancel Button
    //Return to main_cotent activity
    public void cancel(View view) {
        Toast.makeText(MapDisplayActivity.this, "Entry Discarded", Toast.LENGTH_SHORT).show();

        //stop service
        stopService(new Intent(this, TrackingService.class));

        //unBind the service
        unbindService(mConnection);
        mIsBound=false;

        //unregister the reciever
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.unregisterReceiver(mTrackReceiver);

        //close the database
        SQ.close();

        //create intent to go back to mainactivity
        Intent intent = new Intent(MapDisplayActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //get the unit choice and update memberVariable
    public void updateUnitType(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUnit = sp.getString("unit_preference", "-1");

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        //unbind service
        if(!mIsHistoryEntry){
            unbindService(mConnection);
            mIsBound=false;
        }

        //appliction is being force quit
        if(isFinishing()){
            Log.d("testing finish", "is true");
            //stop service
            stopService(new Intent(this, TrackingService.class));

            //unregister the reciever
            LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
            bm.unregisterReceiver(mTrackReceiver);

            //close the database
            SQ.close();


        }
        super.onDestroy();
    }

    //reacieve
    private BroadcastReceiver mTrackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //see where broadcast is coming from
            Boolean isActivityUpdate= intent.getBooleanExtra("isActivityUpdate", false);

            // Get extra data included in the Intent
            //Log.d("receiver", "Got message: ");

            //update entry
            mEntry=mService.getExerciseEntry();

            //draw new trace on map if is a location update and update textview
            if(!isActivityUpdate){
                drawTraceOnMap();
                updateTextView();
            }
            else{
                Log.d("receiver", "Got message: ");
                updateTextView();
            }

        }
    };

    @Override //inflates the menu with the Delete button to the view
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate action bar with delte button if is History Entry
        if(mIsHistoryEntry) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteButton:

                //delete entry on Async Task
                DeleteEntryTask task= new DeleteEntryTask();
                task.execute();

                Toast.makeText(this, "Entry Discarded", Toast.LENGTH_SHORT).show();

                //create intent to go back to mainactivity
                Intent intent = new Intent(MapDisplayActivity.this, MainActivity.class);
                startActivity(intent);



                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    //async task to delete an extry
    public class DeleteEntryTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SQ.removeEntry(mEntryID);

            return null;
        }
    }

    //AsyncTaskLoader for loading entry data
    public class EntryLoader extends AsyncTaskLoader<ExerciseEntry> {

        public EntryLoader (Context context) {
            super(context);
        }

        @Override //load data on seperate thread
        public ExerciseEntry loadInBackground() {
            //get all exercise entries
            ExerciseEntry entry= SQ.fetchEntryByIndex(mEntryID);
            return  entry;
        }
    }

}
