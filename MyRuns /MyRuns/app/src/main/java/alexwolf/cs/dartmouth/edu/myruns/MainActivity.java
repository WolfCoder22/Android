package alexwolf.cs.dartmouth.edu.myruns;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.dartmouth.cs.alexwolf.myruns.backend.registration.Registration;

public class MainActivity extends Activity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapter myViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //register the google Cloud messager
        new GcmRegistrationAsyncTask(this).execute();

        // Define SlidingTabLayout (shown at top)
        // and ViewPager (shown at bottom) in the layout.
        // Get their instances.
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //create fragment list and put in fragments
        fragments= new ArrayList<Fragment>();
        fragments.add(new StartFragment());
        fragments.add(new HistoryFragment());
        fragments.add(new SettingsFragment());

        // use FragmentPagerAdapter to bind the slidingTabLayout and ViewPager together.
        myViewPageAdapter =new ActionTabsViewPagerAdapter(getFragmentManager(),
                fragments);

        viewPager.setAdapter(myViewPageAdapter);

        //Set up on page Listener for the the onFragmentResume() implementation
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float v, final int i2) {

            }

            @Override
            public void onPageSelected(final int position) {
                FragmentInterface fragment = (FragmentInterface) myViewPageAdapter.instantiateItem(viewPager, position);
                //history tab selected exececute implemented method
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }

            }

            @Override
            public void onPageScrollStateChanged(final int position) {
            }
        });

        // make sure the tabs are equally spaced.
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

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

    // Called to save UI state changes at the
    // end of the active lifecycle.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {
        private Registration regService = null;
        private GoogleCloudMessaging gcm;
        private Context context;

        // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
        private static final String SENDER_ID = "91216249540";


        public GcmRegistrationAsyncTask(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(Void... params) {
            if (regService == null) {
                Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://propane-analogy-121223.appspot.com/_ah/api/");
                // end of optional local run code

                regService = builder.build();
            }

            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                String regId = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regId;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.

                regService.register(regId).execute();

            } catch (IOException ex) {
                ex.printStackTrace();
                msg = "Error: " + ex.getMessage();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
        }
    }
}


