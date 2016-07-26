package alexwolf.cs.dartmouth.edu.myruns;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexwolf on 2/22/16.
 */
public class GcmIntentService extends IntentService {
    EntrySQLiteHelper SQ;
    Long mEntryID;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

                showToast(extras.getString("message"));
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //display toast if entry was added
                if(Objects.equals(message, "Added")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }

                //its a entry id
                else{
                    //convert id to long
                    mEntryID= Long.valueOf(message);

                    //get the databse
                    SQ= new EntrySQLiteHelper(getApplicationContext());

                    //delete entry on async task
                    new DeleteEntryTask().execute();

                    Toast.makeText(getApplicationContext(), "Entry #"+message+" deleted", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //async task to delete an extry
    public class DeleteEntryTask extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            //remove entry
            Log.d("entry id", Long.toString(mEntryID));
            SQ.removeEntry(mEntryID);

            //close database
            SQ.close();
            return null;
        }
    }
}
