package alexwolf.cs.dartmouth.edu.myruns;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Created by alexwolf on 2/5/16.
 */
public class TrackingService extends Service implements SensorEventListener {

    protected LocationManager locationManager;
    private final int NOTIFICATION_KEY = 22;
    ExerciseEntry mEntry;
    private final IBinder mBinder = new TrackingBinder();
    public Location mLastLoc;
    public double mDistance;
    public double mClimb;
    public int mCalorie;
    public double mCurrentSpeed;
    public double mAvgSpeed;
    public int mDuration;
    public Location mFirstLocation;
    private boolean mHaveNotFoundFirst;
    private long mStartTime;
    private SensorManager sensorManager;
    public ArrayBlockingQueue<Double> mAccBuffer;
    private int ACCELEROMETER_BLOCK_CAPACITY= 64;
    private ArrayList<Double> mActivityTypes;

    //create the Location Listener
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            //set first location
            if (mHaveNotFoundFirst) {
                mFirstLocation = location;
                mHaveNotFoundFirst = false;
            }

            //get data for exercise entry
            if (mLastLoc != null) {

                //get these variables
                mDistance += (Math.abs(location.distanceTo(mLastLoc))/1000)/1.60934;

                mClimb += (location.getAltitude() - mLastLoc.getAltitude()) / 1.60934;
                mClimb = Math.round(mClimb * 100d) / 100d;

                mCalorie = (int) (mDistance * 1000 / 15.0);

                //get duration of run(hours)
                mDuration = ((int) (location.getTime() - mStartTime)) / 1000;

                //set these variabales
                mCurrentSpeed = location.getSpeed() * 3600 / 1609.34;
                mCurrentSpeed = Math.round(mCurrentSpeed * 100d) / 100d;

                mAvgSpeed = mDistance*3600 /mDuration;

            }

            //update Enetry data
            mEntry.setmDuration(mDuration);
            mEntry.setmDistance(mDistance);
            mEntry.setmClimb(mClimb);
            mEntry.setmCalorie(mCalorie);
            mEntry.setmAvgSpeed(mAvgSpeed);
            mEntry.setmCurrSpeed(mCurrentSpeed);


            //turn loc to LatLng and add to array list
            LatLng loc = fromLocationToLatLng(location);
            mEntry.getmLocationList().add(loc);

            //update last location
            mLastLoc = location;

            //send broadast to map activity updating the map when a location has been recieved
            Intent intent = new Intent("get_data");
            Log.i(getClass().getName(), "Message Sent");

            //send local broadcast to map display activity
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private OnSensorChangedTask mSensorAsyncTask;

    @Override
    public void onSensorChanged(SensorEvent event) {

        //if the event is from an accelerameter
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            //find the magnitude of change
            double m = Math.sqrt(event.values[0] * event.values[0]
                    + event.values[1] * event.values[1]
                    + event.values[2] * event.values[2]);

            try {
                //add magnitude to buffer
                mAccBuffer.add(m);

            } catch (IllegalStateException e) {

                //pause asunce task
                mSensorAsyncTask.cancel(true);

                //make new array twice as big if filled and copy data
                ArrayBlockingQueue<Double> newBuffer = new ArrayBlockingQueue<Double>(
                        mAccBuffer.size() * 2);

                mAccBuffer.drainTo(newBuffer);
                mAccBuffer = newBuffer;

                //add m to new array filled with previous values
                mAccBuffer.add(m);

                //start asynctask again
                mSensorAsyncTask = new OnSensorChangedTask();
                mSensorAsyncTask.execute();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //class for getting the binder
    public class TrackingBinder extends Binder {
        TrackingService getService() {

            // Return trackService binder for client use
            return TrackingService.this;
        }
    }

    //called when service is called by start service
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //set time
        mStartTime = System.currentTimeMillis();

        //set booelan to true
        mHaveNotFoundFirst = true;

        //get data from intent
        int inputType = intent.getIntExtra("inputType", -1);
        int activityType = intent.getIntExtra("activityType", -1);

        setUpNotification();
        initExerciseEntry(inputType, activityType);
        startLocationUpdate();

        //check to see if an automatic entry
        if(inputType==2){

            //initialize block array with capcity of 64 entries
            mAccBuffer = new ArrayBlockingQueue<Double>(64);

            //init sensor mangar with accelermoeter
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

            //start asynctask for sensor data retrieving
            mSensorAsyncTask = new OnSensorChangedTask();
            mSensorAsyncTask.execute();


        }

        return super.onStartCommand(intent, flags, startId);
    }

    //sets the my runs notification
    public void setUpNotification() {

        //create pending intent to go to MapDisplayActivity
        Intent i = new Intent(this, MapDisplayActivity.class);
        i.putExtra("fromNotification", true);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                i, 0);

        //build the notification
        Notification mNotification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_runner)
                        .setContentTitle("My Runs")
                        .setContentText("Recording your path now")
                        .setContentIntent(contentIntent)
                        .build();

        //add flags to notification
        mNotification.flags = mNotification.flags | Notification.FLAG_ONGOING_EVENT;
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;


        //get notificationManager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //notify
        mNotificationManager.notify(NOTIFICATION_KEY, mNotification);

    }

    //create new exercise entry and return
    public void initExerciseEntry(int inputType, int activityType) {

        //create an araylist to store lats and longs of entry
        ArrayList<LatLng> mLatLng = new ArrayList<LatLng>();

        //get current time calender for entry
        Long timeMillis = Calendar.getInstance().getTimeInMillis();

        //create new entry and bind LatLng to it
        mEntry = new ExerciseEntry();
        mEntry.setmLocationList(mLatLng);
        mEntry.setmDateTime(timeMillis);
        mEntry.setmInputType(inputType);

        //set input type if a gps entry
        if(inputType!=2){
            mEntry.setmActivityType(activityType);
        }
        mEntry.setmCurrSpeed(0.0);
    }

    public ExerciseEntry getExerciseEntry() {
        return mEntry;
    }

    //start a location updater
    public void startLocationUpdate() {


        //get the location manager
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(svcName);

        //ask user for location access permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //request updates every 2 seconds or 10 meters
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, locationListener);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 2000, 5, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5, locationListener);

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(getClass().getName(), "onBind()");
        return (mBinder);
    }

    //stops the notification
    public void stopNotification() {
        NotificationManager oldNoti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        oldNoti.cancel(NOTIFICATION_KEY);
    }


    //method that turns a location to a LatLng
    public static LatLng fromLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //stop the notification
        stopNotification();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.removeUpdates(locationListener);

        //stop sensor and asynctask if automatic entry
        if(mEntry.getmInputType()==2){
            mSensorAsyncTask.cancel(true);
            mSensorManager.unregisterListener(this);
        }
    }

    private class OnSensorChangedTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            //initi activity array list
            mActivityTypes= new ArrayList<Double>();

            int blockSize = 0;
            FFT fft = new FFT(ACCELEROMETER_BLOCK_CAPACITY);
            double[] accBlock = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double[] re = accBlock;
            double[] im = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double max;
            ArrayList<Double> featVect= new ArrayList<Double>();

            //run continuously
            while(true){

                // stop async task if it is canceled or service destroyed
                if (isCancelled ()){
                    return null;}

                else{

                    // Dump the buffer into the accBlock
                    try {
                        accBlock[blockSize++] = mAccBuffer.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //compute featVect only if there's at least 64 data points in the array
                    if (blockSize == ACCELEROMETER_BLOCK_CAPACITY) {
                        //set block size back to 0
                        blockSize = 0;

                        //find max value of the block
                        max=findMax(accBlock);

                        //run the fft
                        fft.fft(re, im);

                        for (int i = 0; i < re.length; i++) {
                            double mag = Math.sqrt(re[i] * re[i] + im[i]
                                    * im[i]);
                            //add magnitude to the feature array
                            featVect.add(mag);
                            im[i] = .0; // Clear the field
                        }

                        //add the max to the vector
                        featVect.add(max);

                        //cast arraylist to array for classify method
                        Double[] featVectArray= featVect.toArray(new Double[featVect.size()]);

                        //find the activity type
                        Double activityType;
                        try {
                            activityType=WekaClassifier.classify(featVectArray);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            activityType=3.0;
                        }

                        //add new acticity to activity list
                        mActivityTypes.add(activityType);
                        Log.d("current activity", Double.toString(activityType));

                        //find most frequent
                        Double mostFreq=getMostFrequentActivity(mActivityTypes);

                        //update the entry
                        setEntryActivity(mostFreq);

                        //send broadast to map activity updating the UI
                        Intent intent = new Intent("get_data");
                        intent.putExtra("isActivityUpdate", true);
                        Log.i(getClass().getName(), "Message Sent");

                        //send local broadcast to map display activity
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    }


                }

            }
        }
    }

    private Double getMostFrequentActivity(ArrayList<Double> mActivityTypes) {
        //create int for counting type
        int standing=0;
        int walking=0;
        int running=0;

        //sort activities
        for(Double activity: mActivityTypes){
            if(activity==0.0){
                standing+=1;
            }
            else if(activity==1.0){
                walking+=1;
            }
            else{
                running+=1;
            }
        }
        //return double of most frequent activity
        if(standing> walking&& standing>running){
            return 0.0;
        }
        else if(walking>standing && walking>running){
            return 1.0;
        }
        else if(running> walking&& standing<running){
            return 2.0;
        }
        else if(walking==standing&&walking==running){
            return mActivityTypes.get(mActivityTypes.size()-1);
        }
        else if(walking==running){
            return 2.0;
        }
        else if(walking==standing){
            return 1.0;
        }
        else if(running==standing){
            return 3.0;
        }
        return null;
    }

    //method to set entry activity tye based off classifier result
    private void setEntryActivity(Double activityType) {

        //standing
        if(activityType== 0.0){
            mEntry.setmActivityType(2);
        }
        //walking
        else if(activityType==1.0){
            mEntry.setmActivityType(1);
        }
        //running
        else if(activityType==2.0){
            mEntry.setmActivityType(0);
        }
    }

    //method to find largest vlaue in an array
    private Double findMax(double[] array) {
        Double max= 0.0;

        //for all data
        for(int i=0; i<array.length-1; i++){
            //if the value is greater make it max
            if(array[i]>max){
                max= array[i];
            }
        }
        return max;
    }


}
