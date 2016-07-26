package alexwolf.cs.dartmouth.edu.myruns;

import android.app.Fragment;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by alexwolf on 1/15/16.
 */
public class HistoryFragment extends Fragment implements FragmentInterface {
    public String mUnit;
    View rootView;
    public EntrySQLiteHelper SQLite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        updateUnitType();

        //get the rootview
        rootView = inflater.inflate(R.layout.history_fragment_layout, container, false);

        //make the list view
        updateEntries();

        // Inflate the layout for this fragment
        return rootView;
    }


    //method for updating the listview
    public void updateEntries() {

        //get the database helper
        SQLite = new EntrySQLiteHelper(getActivity().getApplicationContext());

        //get all exercise entries on asyncloader thread
        EntryLoader loader= new EntryLoader(getActivity().getApplicationContext());
        final ExerciseEntry[] entries= loader.loadInBackground();


        //get ExerciseArrayAdaptor
        final ExerciseArrayAdaptor mAdaptor = new ExerciseArrayAdaptor(getActivity(), entries, mUnit);


        //get the list view and bind the adaptor to it
        ListView entryListView = (ListView) rootView.findViewById(R.id.entryList);
        entryListView.setAdapter(mAdaptor);

        // Define the listener interface
        AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //get entry clicked on
                ExerciseEntry entry = entries[position];
                //get entry ID
                Long entryID = entry.getId();

                //get the input type
                int inputType= entry.getmInputType();

                    //if its a manual input
                if(inputType==0){

                    //create intent to start entry Display and pass along entry ID and unitType
                    Intent i = new Intent(getActivity(), EntryDisplay.class);
                    i.putExtra("entryID", entryID);
                    i.putExtra("unitType", mUnit);

                    //start intent to go to entry Display
                    startActivity(i);
                }
                //if it's an automatic or gps input
                else{
                    //create intent to go to mapDisplay
                    Intent i= new Intent(getActivity(), MapDisplayActivity.class);

                    //pass entry ID and boolean for mapDisplayActivity
                    i.putExtra("entryId", entryID);
                    i.putExtra("isHistoryEntry", true);

                    startActivity(i);

                }


            }
        };

        //bind onclick listen to listView
        entryListView.setOnItemClickListener(mListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        updateEntries();
    }

    @Override //update the listview when fragment becomes visible again
    public void fragmentBecameVisible() {
        updateUnitType();
        updateEntries();
    }

    //AsyncTaskLoader for loading entry data
    public class EntryLoader extends AsyncTaskLoader<ExerciseEntry[]>{

        public EntryLoader (Context context) {
            super(context);
        }
        @Override //load data on seperate thread
        public ExerciseEntry[] loadInBackground() {

            //get all exercise entries
            return SQLite.fetchEntries();
        }
    }

    //get the unit choice and update memberVariable
    public void updateUnitType(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mUnit = sp.getString("unit_preference", "-1");

    }

}


