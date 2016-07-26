package alexwolf.cs.dartmouth.edu.myruns;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by alexwolf on 1/17/16.
 */
public class SettingsFragment extends PreferenceFragment implements FragmentInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preferences);

    }

    @Override
    public void fragmentBecameVisible() {
        //do nothing
    }
}