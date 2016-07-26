package alexwolf.cs.dartmouth.edu.stressmeter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int mNavItemID;
    private static final String NAV_ITEM_ID = "navItemId";
    public static MediaPlayer mediaPlayer;
    public static Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start sound for mainscreen
        mediaPlayer = MediaPlayer.create(this, R.raw.splash);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //create vibrator
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(Integer.MAX_VALUE);


        //set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up the drawer and set listner
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //set opening screen to Strss Meter if no saved instance state
        if (null == savedInstanceState) {
            mNavItemID = R.id.StressMeter;
        } else {
            mNavItemID = savedInstanceState.getInt(NAV_ITEM_ID);
        }



        //intiate the NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Create PSMSchudeler
        PSMScheduler.setSchedule(this);

        //navigate to frgament
        navigate(mNavItemID);

        //if the submit button was called close the app
        if(getIntent().getBooleanExtra("Close", false)) {

            Toast.makeText(this, "Stress Level Saved", Toast.LENGTH_SHORT).show();

            //close mediaplayer and vibrator
            v.cancel();
            mediaPlayer.release();

            //close the app
            finish();

        }

    }

    // perform the actual navigation logic, updating the main content fragment etc
    private void navigate(final int mNavItemID) {

        //for each fragment in the nav menu
        switch(mNavItemID){

            //retrieve and display ImageChooseFragment
            case R.id.StressMeter: {
                ImageChooseFragment imageChoose = new ImageChooseFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragcontent, imageChoose).commit();
                break;
            }

            //retrive and display Result fragment
            case R.id.results: {

                //stop the music
                mediaPlayer.release();

                //stop the vibrator
                v.cancel();

                //display results fragment
                ResultsFragment results = new ResultsFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragcontent, results).commit();

                break;
            }

        }



    }

    //getter for the media player
    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        navigate(id);

        //close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemID);
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

        //stop the vibrator
        v.cancel();

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

