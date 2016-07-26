package alexwolf.cs.dartmouth.edu.stressmeter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by alexwolf on 1/23/16.
 */
public class ImageConfirm extends Activity {
    int mImagePosition;
    int mStressNumber;
    ImageView mImageForConfirm;
    int mGridNumber;
    PSM psm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_confirm);
        //create to PSM for getting image resource later
        psm= new PSM();

        //get the position and grid numberof the image
        Intent i= getIntent();
        mImagePosition=i.getIntExtra("Image_Position", 0);
        mGridNumber= i.getIntExtra("Grid_Number", 1);

        //set the image Stress  number based of position
        switch(mImagePosition){
            case 0:
                mStressNumber=6;
                break;
            case 1:
                mStressNumber=8;
                break;
            case 2:
                mStressNumber=14;
                break;
            case 3:
                mStressNumber=16;
                break;
            case 4:
                mStressNumber=5;
                break;
            case 5:
                mStressNumber=7;
                break;
            case 6:
                mStressNumber=13;
                break;
            case 7:
                mStressNumber=15;
                break;
            case 8:
                mStressNumber=2;
                break;
            case 9:
                mStressNumber=4;
                break;
            case 10:
                mStressNumber=10;
                break;
            case 11:
                mStressNumber=12;
                break;
            case 12:
                mStressNumber=1;
                break;
            case 13:
                mStressNumber=3;
                break;
            case 14:
                mStressNumber=9;
                break;
            case 15:
                mStressNumber=11;
                break;
        }

        //get ImageView for picture and set it's image rsource
        mImageForConfirm= (ImageView) findViewById(R.id.imageForConfirm);
        mImageForConfirm.setImageResource(PSM.getGridById(mGridNumber)[mImagePosition]);

    }

    //close the activity if cancel button hit and return to Stress Meter
    public void cancel(View v){
        finish();

    }

    //creat intent to go to StressMeter and pass back the Stress Number and time
    public void submit(View v) throws IOException {

        //get Current Time
        Long time=System.currentTimeMillis();
        writeToCSV(mStressNumber, time);

        //exit the app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Close", true);
        startActivity(intent);
        finish();

    }

    //method to handle writing to the CSV file
    public void writeToCSV(int stressLevel, long time) throws IOException {
        {
            //retrieve file location
            String filepath= Environment.getExternalStorageDirectory().getAbsolutePath()
                    +"/"+ "StressResults.csv";

            try{
                //write data to  csv
                FileWriter writer= new FileWriter(filepath, true);

                writer.append(Long.toString(time));
                writer.append(',');

                writer.append(Integer.toString(stressLevel));
                writer.append(',');

                writer.append('\n');
                writer.flush();
                writer.close();
            }

            catch (Exception e) {
            }

        }

    }

    // Called before subsequent visible lifetimes
    // for an activity process.
    @Override
    public void onRestart(){
        super.onRestart();

    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart(){
        super.onStart();
        // Apply any required UI change now that the Activity is visible.
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume(){
        super.onResume();
    }


    // Called at the end of the active lifetime.
    @Override
    public void onPause(){
        super.onPause();

    }

    // Called at the end of the visible lifetime.
    @Override
    public void onStop(){
        super.onStop();

    }

    // Sometimes called at the end of the full lifetime.
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
